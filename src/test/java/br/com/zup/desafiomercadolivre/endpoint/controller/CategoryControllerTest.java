package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.request.CategoryPostRequestBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static br.com.zup.desafiomercadolivre.util.EntityManagerUtil.findAll;
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postRequest;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Category Controller Test")
@WithUserDetails("email@test.com")
class CategoryControllerTest {

    private final String URL_CATEGORY_REGISTER_NEW = "/category/register-new";

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("Create new mother category, return 200 status code and persist new mother category if given valid mother category")
    void createNewCategory_Return200StatusCodeAndPersistNewMotherCategory_IfGivenValidMotherCategory() throws Exception {
        CategoryPostRequestBody requestBody = new CategoryPostRequestBody("Mother Category", null);

        ResultActions resultActions = postRequest(URL_CATEGORY_REGISTER_NEW, requestBody, mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        List<?> categories = findAll(Category.class, entityManager);

        assertEquals(1, categories.size());

        Category category = (Category) categories.get(0);

        assertEquals(requestBody.getName(), category.getName());
        assertNull(category.getMotherCategory());
    }

    @Test
    @DisplayName("Create new child category, return 200 status code and persist new child category if given valid child category")
    void createNewCategory_Return200StatusCodeAndPersistNewChildCategory_IfGivenValidChildCategory() throws Exception {
        Category motherCategory = new CategoryPostRequestBody("Mother Category", null).toCategory(entityManager);

        entityManager.persist(motherCategory);

        CategoryPostRequestBody requestBody = new CategoryPostRequestBody("Child Category", motherCategory.getId());

        ResultActions resultActions = postRequest(URL_CATEGORY_REGISTER_NEW, requestBody, mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        List<?> categories = findAll(Category.class, entityManager);

        assertEquals(2, categories.size());

        Category category = (Category) categories.get(1);

        assertEquals(requestBody.getName(), category.getName());
        assertEquals(requestBody.getMotherCategoryId(), category.getMotherCategory().getId());
    }

    @Test
    @DisplayName("Create new child category, return 400 status code and dont persist new child category if given invalid mother category id category")
    void createNewCategory_Return400StatusCodeAndDontPersistNewChildCategory_IfGivenInvalidMotherCategoryId() throws Exception {
        Category motherCategory = new CategoryPostRequestBody("Mother Category", null).toCategory(entityManager);

        entityManager.persist(motherCategory);

        CategoryPostRequestBody requestBody = new CategoryPostRequestBody("Child Category", motherCategory.getId() + 1);

        assertBadRequestInvalidCategory(requestBody);

        List<?> categories = findAll(Category.class, entityManager);

        assertEquals(1, categories.size());
    }

    @Test
    @DisplayName("Create new category, return 400 status code if given blank name")
    void createNewCategory_Return400StatusCode_IfGivenBlankName() throws Exception {
        CategoryPostRequestBody requestBody = new CategoryPostRequestBody("", null);

        assertBadRequestInvalidCategory(requestBody);
    }

    @Test
    @DisplayName("Create new category, return 400 status code if given name already registered")
    void createNewCategory_Return400StatusCode_IfGivenNameAlreadyRegistered() throws Exception {
        Category category = new CategoryPostRequestBody("Category", null).toCategory(entityManager);

        entityManager.persist(category);

        CategoryPostRequestBody requestBody = new CategoryPostRequestBody(category.getName(), null);

        assertBadRequestInvalidCategory(requestBody);
    }

    @Test
    @DisplayName("Create new category, return 403 status code when unauthorized user submit request")
    @WithAnonymousUser
    void createNewCategory_Return403StatusCode_WhenUnauthorizedUserSubmitRequest() throws Exception {
        CategoryPostRequestBody requestBody = new CategoryPostRequestBody("Mother Category", null);

        ResultActions resultActions = postRequest(URL_CATEGORY_REGISTER_NEW, requestBody, mockMvc);

        assertEquals(FORBIDDEN.value(), resultActions.andReturn().getResponse().getStatus());
    }

    private void assertBadRequestInvalidCategory(CategoryPostRequestBody requestBody) throws Exception {
        ResultActions resultActions = postRequest(URL_CATEGORY_REGISTER_NEW, requestBody, mockMvc);

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

        Class<? extends Exception> aClass = requireNonNull(resultActions.andReturn().getResolvedException()).getClass();

        assertEquals(MethodArgumentNotValidException.class, aClass);
    }

}
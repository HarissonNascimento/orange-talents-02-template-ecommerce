package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.Question;
import br.com.zup.desafiomercadolivre.model.request.QuestionPostRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static br.com.zup.desafiomercadolivre.util.ProductCreatorUtil.persistProduct;
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postRequest;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Product Question Controller Test")
@WithUserDetails("email@test.com")
class ProductQuestionControllerTest {

    private final String LOGGED_USER_EMAIL = "email@test.com";
    private final String URL_PRODUCT_NEW_QUESTION = "/product/{id}/question";
    private final Category category = new Category("TestCategory", null);

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager.persist(category);
    }

    @Test
    @DisplayName("New user question, return 200 status code and persist new question if given valid question")
    void newUserQuestion_Return200StatusCodeAndPersistNewQuestion_IfGivenValidQuestion() throws Exception {
        List<?> all = findAll(Question.class, entityManager);
        int size = all.size();

        Product product = persistProduct(LOGGED_USER_EMAIL, entityManager, category);

        QuestionPostRequestBody requestBody = new QuestionPostRequestBody("TestQuestion");

        ResultActions resultActions = postRequest(URL_PRODUCT_NEW_QUESTION, product.getId(), requestBody, mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        all = findAll(Question.class, entityManager);

        assertEquals(size + 1, all.size());

        Question question = (Question) all.get(0);

        assertEquals(question.getTitle(), requestBody.getTitle());
    }

    @Test
    @DisplayName("New user question, return 400 status code if given blank title")
    void newUserQuestion_Return400StatusCode_IfGivenBlankTitle() throws Exception {
        Product product = persistProduct(LOGGED_USER_EMAIL, entityManager, category);

        QuestionPostRequestBody requestBody = new QuestionPostRequestBody("");

        ResultActions resultActions = postRequest(URL_PRODUCT_NEW_QUESTION, product.getId(), requestBody, mockMvc);

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

        Class<? extends Exception> aClass = requireNonNull(resultActions.andReturn().getResolvedException()).getClass();

        assertEquals(MethodArgumentNotValidException.class, aClass);
    }


}
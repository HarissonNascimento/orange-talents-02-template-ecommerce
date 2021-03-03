package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static br.com.zup.desafiomercadolivre.util.ProductCreatorUtil.persistProduct;
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postImages;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Product Image Controller Test")
@WithUserDetails("email@test.com")
class ProductImageControllerTest {

    private final String LOGGED_USER_EMAIL = "email@test.com";
    private final String URL_PRODUCT_ADD_IMAGES = "/product/{id}/images";
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
    @DisplayName("Add images, return 200 status code when successful")
    void addImages_Return200StatusCode_WhenSuccessful() throws Exception {

        Product product = persistProduct(LOGGED_USER_EMAIL, entityManager, category);

        MockMultipartFile multipartFile = new MockMultipartFile("fakeImagesTest", "fakeImagesTest.png", "", "fakeImageBytes".getBytes());

        ResultActions resultActions = postImages(URL_PRODUCT_ADD_IMAGES, product.getId(), mockMvc, multipartFile.getBytes());

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    @DisplayName("Add images, return 400 status code if user id not authorized")
    @WithUserDetails("email2@test.com")
    void addImages_Return400StatusCode_IfUserIsNotAuthorized() throws Exception {

        Product product = persistProduct(LOGGED_USER_EMAIL, entityManager, category);

        MockMultipartFile multipartFile = new MockMultipartFile("fakeImagesTest", "fakeImagesTest.png", "", "fakeImageBytes".getBytes());

        ResultActions resultActions = postImages(URL_PRODUCT_ADD_IMAGES, product.getId(), mockMvc, multipartFile.getBytes());

        assertEquals(FORBIDDEN.value(), resultActions.andReturn().getResponse().getStatus());
    }

}
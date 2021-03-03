package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.response.ProductDetailsGetResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static br.com.zup.desafiomercadolivre.util.ProductCreatorUtil.persistProduct;
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.getRequest;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Product Details Controller Test")
@WithUserDetails("email@test.com")
class ProductDetailsControllerTest {

    private final String LOGGED_USER_EMAIL = "email@test.com";
    private final String URL_PRODUCT_DETAILS = "/product/{id}/details";
    private final Category category = new Category("TestCategory", null);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager.persist(category);
    }

    @Test
    @DisplayName("Get product details, return 200 status code and all product details when successful")
    void getProductDetails_Return200StatusCodeAndAllProductDetails_WhenSuccessful() throws Exception {
        Product product = persistProduct(LOGGED_USER_EMAIL, entityManager, category);

        ResultActions resultActions = getRequest(URL_PRODUCT_DETAILS, product.getId(), mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        ProductDetailsGetResponseBody productDetailsGetResponseBody = objectMapper.readValue(jsonResponse, ProductDetailsGetResponseBody.class);

        ProductDetailsGetResponseBody expectedDetails = product.getDetails();

        assertAll(
                () -> assertEquals(productDetailsGetResponseBody.getImageLinks(), expectedDetails.getImageLinks()),
                () -> assertEquals(productDetailsGetResponseBody.getName(), expectedDetails.getName()),
                () -> assertEquals(productDetailsGetResponseBody.getPrice(), expectedDetails.getPrice()),
                () -> assertEquals(productDetailsGetResponseBody.getDescription(), expectedDetails.getDescription()),
                () -> assertEquals(productDetailsGetResponseBody.getRateAverage(), expectedDetails.getRateAverage()),
                () -> assertEquals(productDetailsGetResponseBody.getTotalOpinions(), expectedDetails.getTotalOpinions()),
                () -> assertEquals(productDetailsGetResponseBody.getOpinionsList(), expectedDetails.getOpinionsList()),
                () -> assertEquals(productDetailsGetResponseBody.getQuestionsList(), expectedDetails.getQuestionsList())
        );
    }

}
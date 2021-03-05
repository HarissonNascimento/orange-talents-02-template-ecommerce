package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.enums.GatewayType;
import br.com.zup.desafiomercadolivre.model.request.PurchasePostRequestBody;
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
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static br.com.zup.desafiomercadolivre.util.EntityManagerUtil.findAll;
import static br.com.zup.desafiomercadolivre.util.ProductCreatorUtil.persistProduct;
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postRequest;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FOUND;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Product Purchase Controller Test")
@WithUserDetails("email@test.com")
class ProductPurchaseControllerTest {

    private final String LOGGED_USER_EMAIL = "email@test.com";
    private final String URL_PRODUCT_USER_OPINION = "/product/buy";
    private final Category category = new Category("TestCategory", null);
    private Product product;

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager.persist(category);
        product = persistProduct(LOGGED_USER_EMAIL, entityManager, category);
    }

    @Test
    @DisplayName("Buy product, return 302 status code and persist purchase if given valid pagseguro purchase")
    void buyProduct_Return302StatusCodeAndPersistPurchase_IfGivenValidPagSeguroPurchase() throws Exception {
        List<?> all = findAll(Purchase.class, entityManager);

        int size = all.size();

        PurchasePostRequestBody requestBody = new PurchasePostRequestBody(product.getId(), product.getAmount(), GatewayType.PAGSEGURO);

        ResultActions resultActions = postRequest(URL_PRODUCT_USER_OPINION, requestBody, mockMvc);

        assertEquals(FOUND.value(), resultActions.andReturn().getResponse().getStatus());

        all = findAll(Purchase.class, entityManager);

        assertEquals(size + 1, all.size());

        Purchase purchase = (Purchase) all.get(0);

        assertAll(
                () -> assertEquals(requestBody.getProductId(), purchase.getProduct().getId()),
                () -> assertEquals(requestBody.getGatewayType(), purchase.getGatewayType())
        );
    }

    @Test
    @DisplayName("Buy product, return 302 status code and persist purchase if given valid paypal purchase")
    void buyProduct_Return302StatusCodeAndPersistPurchase_IfGivenValidPayPalPurchase() throws Exception {
        List<?> all = findAll(Purchase.class, entityManager);

        int size = all.size();

        PurchasePostRequestBody requestBody = new PurchasePostRequestBody(product.getId(), product.getAmount(), GatewayType.PAYPAL);

        ResultActions resultActions = postRequest(URL_PRODUCT_USER_OPINION, requestBody, mockMvc);

        assertEquals(FOUND.value(), resultActions.andReturn().getResponse().getStatus());

        all = findAll(Purchase.class, entityManager);

        assertEquals(size + 1, all.size());

        Purchase purchase = (Purchase) all.get(0);

        assertAll(
                () -> assertEquals(requestBody.getProductId(), purchase.getProduct().getId()),
                () -> assertEquals(requestBody.getGatewayType(), purchase.getGatewayType())
        );
    }

    @Test
    @DisplayName("Buyd product, return 400 status code if given invalid product id")
    void buyProduct_Return400StatusCode_IfGivenInvalidProductId() throws Exception {
        long invalidProductId = product.getId() + 10;

        PurchasePostRequestBody requestBody = new PurchasePostRequestBody(invalidProductId, product.getAmount(), GatewayType.PAYPAL);

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("Buyd product, return 400 status code if given amount more than available")
    void buyProduct_Return400StatusCode_IfGivenAmountMoreThanAvailable() throws Exception {
        int invalidAmount = product.getAmount() + 10;

        PurchasePostRequestBody requestBody = new PurchasePostRequestBody(product.getId(), invalidAmount, GatewayType.PAYPAL);

        ResultActions resultActions = postRequest(URL_PRODUCT_USER_OPINION, product.getId(), requestBody, mockMvc);

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

        Class<? extends Exception> aClass = requireNonNull(resultActions.andReturn().getResolvedException()).getClass();

        assertEquals(BindException.class, aClass);
    }

    private void assertBadRequest(PurchasePostRequestBody requestBody) throws Exception {
        ResultActions resultActions = postRequest(URL_PRODUCT_USER_OPINION, product.getId(), requestBody, mockMvc);

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

        Class<? extends Exception> aClass = requireNonNull(resultActions.andReturn().getResolvedException()).getClass();

        assertEquals(MethodArgumentNotValidException.class, aClass);
    }

}
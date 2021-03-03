package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.*;
import br.com.zup.desafiomercadolivre.model.request.CharacteristicPostRequestBody;
import br.com.zup.desafiomercadolivre.model.request.OpinionPostRequestBody;
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
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.zup.desafiomercadolivre.util.EntityManagerUtil.findAll;
import static br.com.zup.desafiomercadolivre.util.EntityManagerUtil.findByString;
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postRequest;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Product Opinion Controller Test")
@WithUserDetails("email@test.com")
class ProductOOpinionControllerTest {

    private final String LOGGED_USER_EMAIL = "email@test.com";
    private final String URL_PRODUCT_USER_OPINION = "/product/{id}/opinion";
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
    @DisplayName("New user opinion, return 200 status code and persist new opinion if given valid opinion")
    void newUserOpinion_Return200StatusCodeAndPersistNewOpinion_IfGivenValidOpinion() throws Exception {

        List<?> all = findAll(Opinion.class, entityManager);
        int size = all.size();

        Product product = persistProductWithUserByEmail(LOGGED_USER_EMAIL);

        OpinionPostRequestBody requestBody = new OpinionPostRequestBody(5, "TestOpinion", "DescriptionTestOpinion");

        ResultActions resultActions = postRequest(URL_PRODUCT_USER_OPINION, product.getId(), requestBody, mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        all = findAll(Opinion.class, entityManager);

        assertEquals(size + 1, all.size());

        Opinion opinion = (Opinion) all.get(0);

        assertAll(
                () -> assertEquals(requestBody.getRate(), opinion.getRate()),
                () -> assertEquals(requestBody.getTitle(), opinion.getTitle()),
                () -> assertEquals(requestBody.getDescription(), opinion.getDescription())
        );
    }

    @Test
    @DisplayName("New user opinion, return 400 status code if given rate less than 1")
    void newUserOpinion_Return400StatusCode_IfGivenRateLessThan1() throws Exception {
        OpinionPostRequestBody requestBody = new OpinionPostRequestBody(0, "TestOpinion", "DescriptionTestOpinion");

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("New user opinion, return 400 status code if given rate greater than 5")
    void newUserOpinion_Return400StatusCode_IfGivenRateGreaterThan5() throws Exception {
        OpinionPostRequestBody requestBody = new OpinionPostRequestBody(6, "TestOpinion", "DescriptionTestOpinion");

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("New user opinion, return 400 status code if given blank title")
    void newUserOpinion_Return400StatusCode_IfGivenBlankTitle() throws Exception {
        OpinionPostRequestBody requestBody = new OpinionPostRequestBody(5, "", "DescriptionTestOpinion");

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("New user opinion, return 400 status code if given description greater than 500")
    void newUserOpinion_Return400StatusCode_IfGivenDescriptionGreaterThan500() throws Exception {
        String bigString =
                "---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|" +
                        "---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|" +
                        "---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|" +
                        "---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|" +
                        "---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|" +
                        "---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|";

        OpinionPostRequestBody requestBody = new OpinionPostRequestBody(5, "", bigString);

        assertBadRequest(requestBody);
    }

    private void assertBadRequest(OpinionPostRequestBody requestBody) throws Exception {
        Product product = persistProductWithUserByEmail(LOGGED_USER_EMAIL);

        ResultActions resultActions = postRequest(URL_PRODUCT_USER_OPINION, product.getId(), requestBody, mockMvc);

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

        Class<? extends Exception> aClass = requireNonNull(resultActions.andReturn().getResolvedException()).getClass();

        assertEquals(MethodArgumentNotValidException.class, aClass);
    }

    private Product persistProductWithUserByEmail(String email) {
        User user = (User) findByString(User.class, email, "email", entityManager)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        List<Characteristic> collectList = getCharacteristicListWithMoreThanThree().stream().map(CharacteristicPostRequestBody::toCharacteristic)
                .collect(Collectors.toList());

        Product product = new Product("TestProduct", 10, BigDecimal.valueOf(10), collectList, "DescriptionProduct", category, user);

        entityManager.persist(product);
        return product;
    }

    private List<CharacteristicPostRequestBody> getCharacteristicListWithMoreThanThree() {
        return Arrays.asList(
                new CharacteristicPostRequestBody("TestCharacteristic1", "DescriptionCharacteristic1"),
                new CharacteristicPostRequestBody("TestCharacteristic2", "DescriptionCharacteristic2"),
                new CharacteristicPostRequestBody("TestCharacteristic3", "DescriptionCharacteristic3")
        );
    }

}
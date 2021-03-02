package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.domain.Characteristic;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.CharacteristicPostRequestBody;
import br.com.zup.desafiomercadolivre.model.request.ProductPostRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
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
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postImages;
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
@DisplayName("Product Controller Test")
@WithUserDetails("email@test.com")
class ProductControllerTest {

    private final String URL_PRODUCT_REGISTER_NEW = "/product/register-new";
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
    @DisplayName("Create new product, return 200 status code and persist new product in database if given valid product")
    void createNewProduct_Return200StatusCodeAndPersistNewProduct_IfGivenValidProduct() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("TestProduct",
                10,
                BigDecimal.valueOf(10),
                getCharacteristicListWithMoreThanThree(),
                "DescriptionProduct",
                category.getId());

        ResultActions resultActions = postRequest(URL_PRODUCT_REGISTER_NEW, requestBody, mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        List<?> products = findAll(Product.class, entityManager);

        assertEquals(1, products.size());

        Product product = (Product) products.get(0);

        assertAll(
                () -> assertEquals(requestBody.getName(), product.getName()),
                () -> assertEquals(requestBody.getAmount(), product.getAmount()),
                () -> assertEquals(requestBody.getPrice(), product.getPrice()),
                () -> assertEquals(requestBody.getDescription(), product.getDescription()),
                () -> assertEquals(requestBody.getIdCategory(), product.getCategory().getId())
        );
    }

    @Test
    @DisplayName("Create new product, return 400 status code and dont persist new product in database if given invalid product")
    void createNewProduct_Return400StatusCodeAndDontPersistNewProduct_IfGivenInvalidProduct() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("",
                0,
                BigDecimal.valueOf(0),
                null,
                "",
                category.getId());

        assertBadRequest(requestBody);
    }


    @Test
    @DisplayName("Create new product, return 400 status code if given blank name")
    void createNewProduct_Return400StatusCode_IfGivenBlankName() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("",
                10,
                BigDecimal.valueOf(10),
                getCharacteristicListWithMoreThanThree(),
                "DescriptionProduct",
                category.getId());

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("Create new product, return 400 status code if dont given price")
    void createNewProduct_Return400StatusCode_IfDontGivenPrice() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("TestProduct",
                10,
                null,
                getCharacteristicListWithMoreThanThree(),
                "DescriptionProduct",
                category.getId());

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("Create new product, return 400 status code if given negative price")
    void createNewProduct_Return400StatusCode_IfGivenNegativePrice() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("TestProduct",
                10,
                BigDecimal.valueOf(-1),
                getCharacteristicListWithMoreThanThree(),
                "DescriptionProduct",
                category.getId());

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("Create new product, return 400 status code if given less than 3 characteristics")
    void createNewProduct_Return400StatusCode_IfGivenLessThan3Characteristics() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("TestProduct",
                10,
                BigDecimal.valueOf(10),
                getCharacteristicListWithLessThanThree(),
                "DescriptionProduct",
                category.getId());

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("Create new product, return 400 status code if given blank description")
    void createNewProduct_Return400StatusCode_IfGivenBlankDescription() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("TestProduct",
                10,
                BigDecimal.valueOf(10),
                getCharacteristicListWithMoreThanThree(),
                "",
                category.getId());

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("Create new product, return 400 status code if dont given category id")
    void createNewProduct_Return400StatusCode_IfDontGivenIdCategory() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("TestProduct",
                10,
                BigDecimal.valueOf(10),
                getCharacteristicListWithMoreThanThree(),
                "DescriptionProduct",
                null
        );

        assertBadRequest(requestBody);
    }

    @Test
    @DisplayName("Create new product, return 403 status code when unauthorized user submit request")
    @WithAnonymousUser
    void createNewProduct_Return403StatusCode_WhenUnauthorizedUserSubmitRequest() throws Exception {
        ProductPostRequestBody requestBody = new ProductPostRequestBody("TestProduct",
                10,
                BigDecimal.valueOf(10),
                getCharacteristicListWithMoreThanThree(),
                "DescriptionProduct",
                category.getId());

        ResultActions resultActions = postRequest(URL_PRODUCT_REGISTER_NEW, requestBody, mockMvc);

        assertEquals(FORBIDDEN.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    @DisplayName("Add images, return 200 status code when successful")
    void addImages_Return200StatusCode_WhenSuccessful() throws Exception {

        Product product = persistProductWithUserByEmail("email@test.com");

        MockMultipartFile multipartFile = new MockMultipartFile("fakeImagesTest", "fakeImagesTest.png", "", "fakeImageBytes".getBytes());

        ResultActions resultActions = postImages(URL_PRODUCT_ADD_IMAGES, product.getId(), mockMvc, multipartFile.getBytes());

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    @DisplayName("Add images, return 400 status code if user id not authorized")
    @WithUserDetails("email2@test.com")
    void addImages_Return400StatusCode_IfUserIsNotAuthorized() throws Exception {

        Product product = persistProductWithUserByEmail("email@test.com");

        MockMultipartFile multipartFile = new MockMultipartFile("fakeImagesTest", "fakeImagesTest.png", "", "fakeImageBytes".getBytes());

        ResultActions resultActions = postImages(URL_PRODUCT_ADD_IMAGES, product.getId(), mockMvc, multipartFile.getBytes());

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
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

    private void assertBadRequest(ProductPostRequestBody requestBody) throws Exception {
        ResultActions resultActions = postRequest(URL_PRODUCT_REGISTER_NEW, requestBody, mockMvc);

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

        Class<? extends Exception> aClass = requireNonNull(resultActions.andReturn().getResolvedException()).getClass();

        assertEquals(MethodArgumentNotValidException.class, aClass);
    }

    private List<CharacteristicPostRequestBody> getCharacteristicListWithMoreThanThree() {
        return Arrays.asList(
                new CharacteristicPostRequestBody("TestCharacteristic1", "DescriptionCharacteristic1"),
                new CharacteristicPostRequestBody("TestCharacteristic2", "DescriptionCharacteristic2"),
                new CharacteristicPostRequestBody("TestCharacteristic3", "DescriptionCharacteristic3")
        );
    }

    private List<CharacteristicPostRequestBody> getCharacteristicListWithLessThanThree() {
        return Arrays.asList(
                new CharacteristicPostRequestBody("TestCharacteristic1", "DescriptionCharacteristic1"),
                new CharacteristicPostRequestBody("TestCharacteristic2", "DescriptionCharacteristic2")
        );
    }

}

package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.domain.Characteristic;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.CharacteristicPostRequestBody;
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
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.zup.desafiomercadolivre.util.EntityManagerUtil.findByString;
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postImages;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.*;

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

        Product product = persistProductWithUserByEmail(LOGGED_USER_EMAIL);

        MockMultipartFile multipartFile = new MockMultipartFile("fakeImagesTest", "fakeImagesTest.png", "", "fakeImageBytes".getBytes());

        ResultActions resultActions = postImages(URL_PRODUCT_ADD_IMAGES, product.getId(), mockMvc, multipartFile.getBytes());

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    @DisplayName("Add images, return 400 status code if user id not authorized")
    @WithUserDetails("email2@test.com")
    void addImages_Return400StatusCode_IfUserIsNotAuthorized() throws Exception {

        Product product = persistProductWithUserByEmail(LOGGED_USER_EMAIL);

        MockMultipartFile multipartFile = new MockMultipartFile("fakeImagesTest", "fakeImagesTest.png", "", "fakeImageBytes".getBytes());

        ResultActions resultActions = postImages(URL_PRODUCT_ADD_IMAGES, product.getId(), mockMvc, multipartFile.getBytes());

        assertEquals(FORBIDDEN.value(), resultActions.andReturn().getResponse().getStatus());
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
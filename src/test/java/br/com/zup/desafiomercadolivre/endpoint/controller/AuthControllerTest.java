package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.TokenPostRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postRequestWithCsrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Auth Controller Test")
class AuthControllerTest {

    private final String URL_AUTH_AUTHENTICATE = "/auth/authenticate";
    private final String userPassword = "passwordtest";
    private final User user = new User("usertest@email.com", userPassword);

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager.persist(user);
    }

    @Test
    @DisplayName("Authenticate, return 200 status code when successful")
    void authenticate_Return200StatusCode_WhenSuccessful() throws Exception {
        TokenPostRequestBody requestBody = new TokenPostRequestBody(user.getEmail(), userPassword);

        ResultActions resultActions = postRequestWithCsrf(URL_AUTH_AUTHENTICATE, requestBody, mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("Bearer"));
    }

    @Test
    @DisplayName("Authenticate, return 403 status code if given invalid credentials")
    void authenticate_Return403StatusCode_IfGivenInvalidCredentials() throws Exception {
        TokenPostRequestBody requestBody = new TokenPostRequestBody("invalidEmail@email.com", "invalidPassword");

        ResultActions resultActions = postRequestWithCsrf(URL_AUTH_AUTHENTICATE, requestBody, mockMvc);

        assertEquals(FORBIDDEN.value(), resultActions.andReturn().getResponse().getStatus());
    }

}
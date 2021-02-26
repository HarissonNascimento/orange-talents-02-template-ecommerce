package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.UserPostRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.zup.desafiomercadolivre.util.EntityManagerUtil.findAll;
import static br.com.zup.desafiomercadolivre.util.EntityManagerUtil.findFirst;
import static br.com.zup.desafiomercadolivre.util.RequisitionBuilder.postRequest;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("User Controller Test")
class UserControllerTest {

    private final String URL_USER_REGISTER_NEW = "/user/register-new";

    @Autowired
    MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Create new user, return status code 200 and persist new user in database if given valid user")
    void createNewUser_Return200StatusCodeAndPersistNewUser_IfGivenValidUser() throws Exception {
        UserPostRequestBody requestBody = new UserPostRequestBody("test@email", "testPassword");

        ResultActions resultActions = postRequest(URL_USER_REGISTER_NEW, requestBody, objectMapper, mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        List<?> users = findAll(User.class, entityManager);

        assertEquals(1, users.size());

        User user = (User) users.get(0);

        assertEquals(requestBody.getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Create new user, return status code 400 and dont persist new user in database if given invalid user")
    void createNewUser_Return400StatusCodeAndDontPersistNewUser_IfGivenInvalidUser() throws Exception {
        UserPostRequestBody requestBody = new UserPostRequestBody("", "");

        ResultActions resultActions = postRequest(URL_USER_REGISTER_NEW, requestBody, objectMapper, mockMvc);

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

        List<?> users = findAll(User.class, entityManager);

        assertEquals(0, users.size());
    }

    @Test
    @DisplayName("Create new user, return 400 status code if given blank login")
    void createNewUser_Return400StatusCode_IfGivenBlankLogin() throws Exception {
        UserPostRequestBody requestBody = new UserPostRequestBody("", "testPassword");

        assertBadRequestInvalidUser(requestBody);
    }

    @Test
    @DisplayName("Create new user, return 400 status code if dont given valid email login")
    void createNewUser_Return400StatusCode_IfDontGivenValidEmailLogin() throws Exception {
        UserPostRequestBody requestBody = new UserPostRequestBody("testInvalidEmail", "testPassword");

        assertBadRequestInvalidUser(requestBody);
    }

    @Test
    @DisplayName("Create new user, return 400 status code if given blank password")
    void createNewUser_Return400StatusCode_IfGivenBlankPassword() throws Exception {
        UserPostRequestBody requestBody = new UserPostRequestBody("test@email.com", "");

        assertBadRequestInvalidUser(requestBody);
    }

    @Test
    @DisplayName("Create new user, return 400 status code if given password less than six characters")
    void createNewUser_Return400StatusCode_IfGivenPasswordLessThanSixCharacters() throws Exception {
        UserPostRequestBody requestBody = new UserPostRequestBody("test@email.com", "12345");

        assertBadRequestInvalidUser(requestBody);
    }

    @Test
    @DisplayName("Create new user, return 400 status code if given email already registered")
    void createNewUser_Return400StatusCode_IfGivenEmailAlreadyRegistered() throws Exception {
        UserPostRequestBody requestBody = new UserPostRequestBody("test@email.com", "validpassword");

        entityManager.persist(requestBody.toUser());

        assertBadRequestInvalidUser(requestBody);
    }

    @Test
    @DisplayName("Create new user, return not future date when successful")
    void createNewUser_ReturnNotFutureDate_WhenSuccessful() throws Exception {
        UserPostRequestBody requestBody = new UserPostRequestBody("test@email", "testPassword");

        ResultActions resultActions = postRequest(URL_USER_REGISTER_NEW, requestBody, objectMapper, mockMvc);

        assertEquals(OK.value(), resultActions.andReturn().getResponse().getStatus());

        User user = (User) findFirst(User.class, entityManager)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        assertFalse(user.getRegistrationTime().isAfter(LocalDateTime.now()));
    }

    private void assertBadRequestInvalidUser(UserPostRequestBody requestBody) throws Exception {
        ResultActions resultActions = postRequest(URL_USER_REGISTER_NEW, requestBody, objectMapper, mockMvc);

        assertEquals(BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

        Class<? extends Exception> aClass = requireNonNull(resultActions.andReturn().getResolvedException()).getClass();

        assertEquals(MethodArgumentNotValidException.class, aClass);
    }

}
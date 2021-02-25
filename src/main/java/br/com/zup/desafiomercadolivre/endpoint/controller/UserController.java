package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.UserPostRequestBody;
import br.com.zup.desafiomercadolivre.model.response.UserPostResponseBody;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/register-new")
    @Transactional
    public UserPostResponseBody createNewUser(@RequestBody @Valid UserPostRequestBody userPostRequestBody) {
        User user = userPostRequestBody.toUser();
        entityManager.persist(user);
        return new UserPostResponseBody().toUserPostResponseBody(user);
    }
}

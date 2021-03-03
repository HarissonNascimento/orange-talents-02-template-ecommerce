package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.Question;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.QuestionPostRequestBody;
import br.com.zup.desafiomercadolivre.model.response.QuestionPostResponseBody;
import br.com.zup.desafiomercadolivre.util.Emails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductQuestionController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Emails email;

    @PostMapping("/{id}/question")
    @Transactional
    public List<QuestionPostResponseBody> newUserQuestion(@PathVariable("id") Long id,
                                                          @RequestBody @Valid QuestionPostRequestBody questionPostRequestBody,
                                                          @AuthenticationPrincipal User user) {

        Product product = Optional.ofNullable(entityManager.find(Product.class, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Question question = questionPostRequestBody.toQuestion(product, user);

        product.associateQuestion(question);

        entityManager.merge(product);

        email.send(question);

        return product.getQuestions().stream().map(q -> new QuestionPostResponseBody().toQuestionPostResponseBody(q)).collect(Collectors.toList());
    }
}

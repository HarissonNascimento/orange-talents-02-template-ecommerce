package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Opinion;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.OpinionPostRequestBody;
import br.com.zup.desafiomercadolivre.model.response.OpinionPostResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductOOpinionController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/{id}/opinion")
    @Transactional
    public OpinionPostResponseBody newUserOpinion(@PathVariable("id") Long id,
                                                  @RequestBody @Valid OpinionPostRequestBody opinionPostRequestBody,
                                                  @AuthenticationPrincipal User user) {

        Product product = Optional.ofNullable(entityManager.find(Product.class, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Opinion opinion = opinionPostRequestBody.toOpinion(product, user);

        entityManager.persist(opinion);

        return new OpinionPostResponseBody().toOpinionPostResponseBody(opinion);
    }
}

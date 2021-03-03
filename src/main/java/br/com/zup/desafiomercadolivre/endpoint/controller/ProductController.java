package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.ProductPostRequestBody;
import br.com.zup.desafiomercadolivre.model.response.ProductPostResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/register-new")
    @Transactional
    public ProductPostResponseBody createNewProduct(@RequestBody @Valid ProductPostRequestBody productPostRequestBody,
                                                    @AuthenticationPrincipal User user) {
        Product product = productPostRequestBody.toProduct(entityManager, user.getId());
        entityManager.persist(product);
        return new ProductPostResponseBody().toProductPostResponseBody(product);
    }

}

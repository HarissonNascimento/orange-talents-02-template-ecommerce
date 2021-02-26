package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.request.CategoryPostRequestBody;
import br.com.zup.desafiomercadolivre.model.response.CategoryPostResponseBody;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/register-new")
    @Transactional
    public CategoryPostResponseBody createNewCategory(@RequestBody @Valid CategoryPostRequestBody categoryPostRequestBody) {
        Category category = categoryPostRequestBody.toCategory(entityManager);
        entityManager.persist(category);
        return new CategoryPostResponseBody().toCategoryPostResponseBody(category);
    }
}

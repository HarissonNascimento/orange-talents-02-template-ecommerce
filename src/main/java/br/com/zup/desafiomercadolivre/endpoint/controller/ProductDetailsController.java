package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.response.ProductDetailsGetResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductDetailsController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/{id}/details")
    public ProductDetailsGetResponseBody getProductDetails(@PathVariable("id") Long id) {

        Product product = Optional.ofNullable(entityManager.find(Product.class, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return product.getDetails();
    }
}

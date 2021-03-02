package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.NewImagesPostRequestBody;
import br.com.zup.desafiomercadolivre.model.request.ProductPostRequestBody;
import br.com.zup.desafiomercadolivre.model.response.NewImagesPostResponseBody;
import br.com.zup.desafiomercadolivre.model.response.ProductPostResponseBody;
import br.com.zup.desafiomercadolivre.util.Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/product")
public class ProductController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Uploader uploaderFake;

    @PostMapping("/register-new")
    @Transactional
    public ProductPostResponseBody createNewProduct(@RequestBody @Valid ProductPostRequestBody productPostRequestBody,
                                                    @AuthenticationPrincipal User user) {
        Product product = productPostRequestBody.toProduct(entityManager, user.getId());
        entityManager.persist(product);
        return new ProductPostResponseBody().toProductPostResponseBody(product);
    }

    @PostMapping("/{id}/images")
    @Transactional
    public NewImagesPostResponseBody addImages(@PathVariable("id") Long id,
                                               @Valid NewImagesPostRequestBody newImagesPostRequestBody,
                                               @AuthenticationPrincipal User user) {

        TypedQuery<Product> query = entityManager.createQuery("select p from Product p join p.user u where p.id = :idProduct and u.id = :idUser", Product.class);
        query.setParameter("idProduct", id);
        query.setParameter("idUser", user.getId());

        List<Product> resultList = query.getResultList();

        if (resultList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product does not exist our your not authorized to execute this request");
        }

        Product product = resultList.get(0);

        Set<String> links = uploaderFake.send(newImagesPostRequestBody.getImages());

        product.associateImages(links);

        entityManager.merge(product);

        return new NewImagesPostResponseBody().toNewImagesPostResponseBody(product);
    }
}

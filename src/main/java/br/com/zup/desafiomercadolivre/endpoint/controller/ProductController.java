package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Opinion;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.ImagesPostRequestBody;
import br.com.zup.desafiomercadolivre.model.request.OpinionPostRequestBody;
import br.com.zup.desafiomercadolivre.model.request.ProductPostRequestBody;
import br.com.zup.desafiomercadolivre.model.response.ImagesPostResponseBody;
import br.com.zup.desafiomercadolivre.model.response.OpinionPostResponseBody;
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
import java.util.Optional;
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
    public ImagesPostResponseBody addImages(@PathVariable("id") Long id,
                                            @Valid ImagesPostRequestBody imagesPostRequestBody,
                                            @AuthenticationPrincipal User user) {

        Product product = findProductByIdAndUserId(id, user.getId());

        Set<String> links = uploaderFake.send(imagesPostRequestBody.getImages());

        product.associateImages(links);

        entityManager.merge(product);

        return new ImagesPostResponseBody().toImagesPostResponseBody(product);
    }

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

    private Product findProductByIdAndUserId(Long id, Long userId) {
        TypedQuery<Product> query = entityManager.createQuery("select p from Product p join p.user u where p.id = :idProduct and u.id = :idUser", Product.class);
        query.setParameter("idProduct", id);
        query.setParameter("idUser", userId);

        List<Product> resultList = query.getResultList();

        if (resultList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your not authorized to execute this request");
        }

        return resultList.get(0);
    }
}

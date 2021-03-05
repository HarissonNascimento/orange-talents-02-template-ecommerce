package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.PurchasePostRequestBody;
import br.com.zup.desafiomercadolivre.util.Emails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequestMapping("/product")
public class ProductPurchaseController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Emails emails;

    @PostMapping("/buy")
    @Transactional
    public ResponseEntity<?> buyProduct(@RequestBody @Valid PurchasePostRequestBody purchasePostRequestBody,
                                        @AuthenticationPrincipal User user,
                                        UriComponentsBuilder uriComponentsBuilder) throws BindException {

        Product product = Optional.ofNullable(entityManager.find(Product.class, purchasePostRequestBody.getProductId()))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST));

        Purchase purchase = purchasePostRequestBody.toPurchase(product, user);

        if (product.isAmountReducible(purchase.getAmount())) {

            purchase.startPurchase();

            entityManager.persist(purchase);

            emails.send(purchase);

            String returnURL = purchase.getGatewayType().returnUrl(uriComponentsBuilder, purchase.getId());

            return new ResponseEntity<>(returnURL, FOUND);
        }

        BindException dontHaveStock = new BindException(purchase.getAmount(), "productBuyPostRequestBody");
        dontHaveStock.reject(BAD_REQUEST.toString(), "It was not possible to make the purchase, we have no stock available!");

        throw dontHaveStock;

    }
}

package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.util.NewPurchaseEvent;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.interfaces.TransactionPostRequest;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.request.PagSeguroTransactionPostRequest;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.request.PayPalTransactionPostRequest;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.response.TransactionPostResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
public class ProductPaymentController {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private NewPurchaseEvent newPurchaseEvent;

    @PostMapping("/pagseguro-return/{id}")
    @Transactional
    public TransactionPostResponseBody pagseguroPayment(@PathVariable("id") Long purchaseId,
                                                        @Valid PagSeguroTransactionPostRequest pagSeguroTransactionPostRequest) {

        return processTransaction(purchaseId, pagSeguroTransactionPostRequest);

    }

    @PostMapping("/paypal-return/{id}")
    @Transactional
    public TransactionPostResponseBody paypalPayment(@PathVariable("id") Long purchaseId,
                                                     @Valid PayPalTransactionPostRequest payPalTransactionPostRequest) {

        return processTransaction(purchaseId, payPalTransactionPostRequest);

    }

    private TransactionPostResponseBody processTransaction(Long purchaseId, TransactionPostRequest transactionPostRequest) {

        Purchase purchase = Optional.ofNullable(entityManager.find(Purchase.class, purchaseId))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST));

        purchase.addTransaction(transactionPostRequest);

        entityManager.merge(purchase);

        newPurchaseEvent.process(purchase, transactionPostRequest);

        return new TransactionPostResponseBody().toTransactionPostResponseBody(purchase);
    }
}

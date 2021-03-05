package br.com.zup.desafiomercadolivre.util;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.domain.Transaction;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.interfaces.TransactionPostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Service
public class NewPurchaseEvent {

    @Autowired
    private Emails emails;

    @Autowired
    private Set<SuccessPurchaseEvent> successPurchaseEvents;

    public void process(Purchase purchase, TransactionPostRequest transactionPostRequest) {

        Transaction transaction = transactionPostRequest.toTransaction(purchase);

        if (purchase.successfullyProcessed()) {
            successPurchaseEvents.forEach(e -> e.process(purchase));
            emails.send(purchase, transaction);
        } else {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
            emails.send(purchase, uriComponentsBuilder);
        }
    }
}

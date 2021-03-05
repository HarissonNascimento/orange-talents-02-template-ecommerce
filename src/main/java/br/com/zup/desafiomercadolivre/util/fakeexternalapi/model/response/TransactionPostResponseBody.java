package br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.response;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.domain.Transaction;

import java.util.Set;

public class TransactionPostResponseBody {

    private Long purchaseId;
    private Long buyerId;
    private Set<Transaction> statusPurchase;

    public Long getPurchaseId() {
        return purchaseId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public Set<Transaction> getStatusPurchase() {
        return statusPurchase;
    }


    public TransactionPostResponseBody toTransactionPostResponseBody(Purchase purchase) {
        this.purchaseId = purchase.getId();
        this.buyerId = purchase.getUser().getId();
        this.statusPurchase = purchase.getTransactions();
        return this;
    }
}

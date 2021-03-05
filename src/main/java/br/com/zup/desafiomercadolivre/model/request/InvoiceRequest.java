package br.com.zup.desafiomercadolivre.model.request;

import javax.validation.constraints.NotNull;

public class InvoiceRequest {

    @NotNull
    private Long purchaseId;
    @NotNull
    private Long buyerId;

    public InvoiceRequest(@NotNull Long purchaseId, @NotNull Long buyerId) {
        this.purchaseId = purchaseId;
        this.buyerId = buyerId;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    @Override
    public String toString() {
        return "InvoiceRequest{" +
                "purchaseId=" + purchaseId +
                ", buyerId=" + buyerId +
                '}';
    }
}

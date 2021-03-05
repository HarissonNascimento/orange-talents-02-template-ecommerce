package br.com.zup.desafiomercadolivre.model.request;

import javax.validation.constraints.NotNull;

public class RankingRequest {

    @NotNull
    private Long purchaseId;
    @NotNull
    private Long sellerId;

    public RankingRequest(@NotNull Long purchaseId, @NotNull Long sellerId) {
        this.purchaseId = purchaseId;
        this.sellerId = sellerId;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    @Override
    public String toString() {
        return "RankingRequest{" +
                "purchaseId=" + purchaseId +
                ", sellerId=" + sellerId +
                '}';
    }
}

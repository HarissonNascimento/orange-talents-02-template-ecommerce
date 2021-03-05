package br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.request;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.domain.Transaction;
import br.com.zup.desafiomercadolivre.model.enums.StatusPurchase;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.interfaces.TransactionPostRequest;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PayPalTransactionPostRequest implements TransactionPostRequest {

    @NotBlank
    private String transactionGatewayId;
    @NotNull
    @Min(0)
    @Max(1)
    private int status;

    public PayPalTransactionPostRequest(@NotBlank String transactionGatewayId,
                                        @NotNull @Min(0) @Max(1) int status) {
        this.transactionGatewayId = transactionGatewayId;
        this.status = status;
    }

    public String getTransactionGatewayId() {
        return transactionGatewayId;
    }

    public Integer getStatus() {
        return status;
    }

    public Transaction toTransaction(Purchase purchase){
        StatusPurchase statusPurchase = this.status == 0 ? StatusPurchase.ERROR : StatusPurchase.SUCCESSFUL;
        return new Transaction(statusPurchase, this.transactionGatewayId, purchase);
    }
}

package br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.request;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.domain.Transaction;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.enums.PagSeguroStatusReturn;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.interfaces.TransactionPostRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PagSeguroTransactionPostRequest implements TransactionPostRequest {

    @NotBlank
    private String transactionGatewayId;
    @NotNull
    private PagSeguroStatusReturn status;

    public PagSeguroTransactionPostRequest(@NotBlank String transactionGatewayId,
                                           @NotNull PagSeguroStatusReturn status) {
        this.transactionGatewayId = transactionGatewayId;
        this.status = status;
    }

    public String getTransactionGatewayId() {
        return transactionGatewayId;
    }

    public PagSeguroStatusReturn getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "PaymentPostRequest{" +
                "transactionId='" + transactionGatewayId + '\'' +
                ", status=" + status +
                '}';
    }

    public Transaction toTransaction(Purchase purchase) {
        return new Transaction(this.status.processStatus(), this.transactionGatewayId, purchase);
    }
}

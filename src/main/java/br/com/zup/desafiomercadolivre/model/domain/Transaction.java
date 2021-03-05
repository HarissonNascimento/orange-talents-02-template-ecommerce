package br.com.zup.desafiomercadolivre.model.domain;

import br.com.zup.desafiomercadolivre.model.enums.StatusPurchase;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private StatusPurchase processStatus;
    @NotBlank
    private String transactionGatewayId;
    @NotNull
    @Valid
    @ManyToOne
    @JsonIgnore
    private Purchase purchase;
    @NotNull
    private LocalDateTime transactionInstant;


    public Transaction(@NotNull StatusPurchase processStatus,
                       @NotBlank String transactionGatewayId,
                       @NotNull @Valid Purchase purchase) {

        this.processStatus = processStatus;
        this.transactionGatewayId = transactionGatewayId;
        this.purchase = purchase;
        this.transactionInstant = LocalDateTime.now();
    }

    @Deprecated
    protected Transaction() {
    }

    public Long getId() {
        return id;
    }

    public StatusPurchase getProcessStatus() {
        return processStatus;
    }

    public String getTransactionGatewayId() {
        return transactionGatewayId;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public LocalDateTime getTransactionInstant() {
        return transactionInstant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isSuccessfullyConcluded() {
        return this.processStatus.equals(StatusPurchase.SUCCESSFUL);
    }
}

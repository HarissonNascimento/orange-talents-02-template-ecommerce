package br.com.zup.desafiomercadolivre.model.domain;

import br.com.zup.desafiomercadolivre.model.enums.GatewayType;
import br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.interfaces.TransactionPostRequest;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    @Valid
    private Product product;
    @NotNull
    private Integer amount;
    @ManyToOne
    @NotNull
    @Valid
    private User user;
    @Enumerated
    @NotNull
    private GatewayType gatewayType;
    @Enumerated
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.MERGE)
    private Set<Transaction> transactions = new HashSet<>();

    public Purchase(@NotNull @Valid Product product,
                    @NotNull Integer amount,
                    @NotNull @Valid User user,
                    @NotNull GatewayType gatewayType) {
        this.product = product;
        this.amount = amount;
        this.user = user;
        this.gatewayType = gatewayType;
    }

    @Deprecated
    protected Purchase() {
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public GatewayType getGatewayType() {
        return gatewayType;
    }

    public void startPurchase() {
        this.product.slaughterStock(this.amount);
    }

    public void addTransaction(@Valid TransactionPostRequest transactionPostRequest) {
        Transaction transaction = transactionPostRequest.toTransaction(this);

        Assert.isTrue(!this.transactions.contains(transaction), "Transaction already exists! ");

        Assert.isTrue(!successfullyProcessed(), "This purchase has already been successfully completed!");

        this.transactions.add(transaction);
    }

    public Set<Transaction> getSuccessfullyTransactions() {
        return this.transactions.stream()
                .filter(Transaction::isSuccessfullyConcluded)
                .collect(Collectors.toSet());
    }

    public boolean successfullyProcessed() {
        return !getSuccessfullyTransactions().isEmpty();
    }
}

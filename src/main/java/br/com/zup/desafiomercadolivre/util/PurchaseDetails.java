package br.com.zup.desafiomercadolivre.util;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;

import java.math.BigDecimal;

public class PurchaseDetails {


    private Long purchaseId;
    private String buyerEmail;
    private Integer amount;
    private String paymentType;
    private String productName;
    private BigDecimal productPrice;


    public PurchaseDetails toPurchaseDetails(Purchase purchase) {
        this.purchaseId = purchase.getId();
        this.buyerEmail = purchase.getUser().getEmail();
        this.amount = purchase.getAmount();
        this.paymentType = purchase.getGatewayType().toString();
        this.productName = purchase.getProduct().getName();
        this.productPrice = purchase.getProduct().getPrice();
        return this;
    }

    @Override
    public String toString() {
        return "PurchaseDetails{" +
                "purchaseId=" + purchaseId +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", amount=" + amount +
                ", paymentType='" + paymentType + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }
}

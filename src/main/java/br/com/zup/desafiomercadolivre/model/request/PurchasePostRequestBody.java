package br.com.zup.desafiomercadolivre.model.request;

import br.com.zup.desafiomercadolivre.annotation.ExistById;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.enums.GatewayType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class PurchasePostRequestBody {

    @PositiveOrZero
    @NotNull
    private Integer amount;
    @NotNull
    @ExistById(domainClass = Product.class, idFieldName = "id")
    private Long productId;
    @NotNull
    private GatewayType gatewayType;

    public PurchasePostRequestBody(@NotNull Long productId,
                                   @PositiveOrZero @NotNull Integer amount,
                                   @NotNull GatewayType gatewayType) {
        this.amount = amount;
        this.productId = productId;
        this.gatewayType = gatewayType;
    }

    public Integer getAmount() {
        return amount;
    }

    public Long getProductId() {
        return productId;
    }

    public GatewayType getGatewayType() {
        return gatewayType;
    }


    public Purchase toPurchase(Product product, User user) {
        return new Purchase(product, this.amount, user, this.gatewayType);
    }
}

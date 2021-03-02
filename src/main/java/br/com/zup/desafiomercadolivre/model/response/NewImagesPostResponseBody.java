package br.com.zup.desafiomercadolivre.model.response;

import br.com.zup.desafiomercadolivre.model.domain.Product;

import java.util.Set;
import java.util.stream.Collectors;

public class NewImagesPostResponseBody {
    private Long productId;
    private Long userId;
    private Set<ProductImagePostResponseBody> images;

    public Long getProductId() {
        return productId;
    }

    public Long getUserId() {
        return userId;
    }

    public Set<ProductImagePostResponseBody> getImages() {
        return images;
    }

    public NewImagesPostResponseBody toNewImagesPostResponseBody(Product product) {
        this.productId = product.getId();
        this.userId = product.getUser().getId();
        this.images = product.getImages().stream()
                .map(i -> new ProductImagePostResponseBody().toProductImagePostResponseBody(i))
                .collect(Collectors.toSet());
        return this;
    }
}

package br.com.zup.desafiomercadolivre.model.response;

import br.com.zup.desafiomercadolivre.model.domain.ProductImage;

public class ProductImagePostResponseBody {

    private Long id;
    private String link;

    public Long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public ProductImagePostResponseBody toProductImagePostResponseBody(ProductImage productImage) {
        this.id = productImage.getId();
        this.link = productImage.getLink();
        return this;
    }
}

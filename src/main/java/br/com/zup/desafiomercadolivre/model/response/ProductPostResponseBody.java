package br.com.zup.desafiomercadolivre.model.response;

import br.com.zup.desafiomercadolivre.model.domain.Product;

public class ProductPostResponseBody {

    private Long id;
    private Long userId;
    private Long categoryId;
    private String name;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public ProductPostResponseBody toProductPostResponseBody(Product product) {
        this.id = product.getId();
        this.userId = product.getUser().getId();
        this.categoryId = product.getCategory().getId();
        this.name = product.getName();
        return this;
    }
}

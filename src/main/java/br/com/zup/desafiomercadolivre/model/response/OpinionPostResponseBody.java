package br.com.zup.desafiomercadolivre.model.response;

import br.com.zup.desafiomercadolivre.model.domain.Opinion;

public class OpinionPostResponseBody {

    private String title;
    private Long id;
    private Long productId;
    private Long userId;

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getUserId() {
        return userId;
    }

    public OpinionPostResponseBody toOpinionPostResponseBody(Opinion opinion) {
        this.title = opinion.getTitle();
        this.id = opinion.getId();
        this.productId = opinion.getProduct().getId();
        this.userId = opinion.getUser().getId();

        return this;
    }
}

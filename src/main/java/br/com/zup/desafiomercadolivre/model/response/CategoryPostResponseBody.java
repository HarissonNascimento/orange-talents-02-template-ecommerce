package br.com.zup.desafiomercadolivre.model.response;

import br.com.zup.desafiomercadolivre.model.domain.Category;

public class CategoryPostResponseBody {

    private String categoryName;
    private Long categoryId;

    public String getCategoryName() {
        return categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public CategoryPostResponseBody toCategoryPostResponseBody(Category category) {
        this.categoryName = category.getName();
        this.categoryId = category.getId();
        return this;
    }
}

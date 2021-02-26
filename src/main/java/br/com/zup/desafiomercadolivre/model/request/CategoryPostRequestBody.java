package br.com.zup.desafiomercadolivre.model.request;

import br.com.zup.desafiomercadolivre.annotation.ExistById;
import br.com.zup.desafiomercadolivre.annotation.UniqueValue;
import br.com.zup.desafiomercadolivre.model.domain.Category;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class CategoryPostRequestBody {

    @NotBlank
    @UniqueValue(domainClass = Category.class, fieldName = "name", message = "The name already taken!")
    private final String name;
    @Positive
    @ExistById(domainClass = Category.class, idFieldName = "id", message = "Category not found")
    private final Long motherCategoryId;

    public CategoryPostRequestBody(@NotBlank String name, @Positive Long motherCategoryId) {
        this.name = name;
        this.motherCategoryId = motherCategoryId;
    }

    public String getName() {
        return name;
    }

    public Long getMotherCategoryId() {
        return motherCategoryId;
    }

    public Category toCategory(EntityManager entityManager) {
        Category category = null;
        if (this.motherCategoryId != null) {
            category = findCategory(entityManager, this.motherCategoryId);
        }
        return new Category(this.name, category);
    }

    private Category findCategory(EntityManager entityManager, Long motherCategoryId) {
        return entityManager.find(Category.class, motherCategoryId);
    }
}

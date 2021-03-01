package br.com.zup.desafiomercadolivre.model.request;

import br.com.zup.desafiomercadolivre.annotation.ExistById;
import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.domain.Characteristic;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductPostRequestBody {

    @NotBlank
    private String name;
    @NotNull
    @PositiveOrZero
    private Integer amount;
    @NotNull
    @Positive
    private BigDecimal price;
    @Size(min = 3)
    @NotEmpty
    @Valid
    private List<CharacteristicPostRequestBody> characteristics;
    @NotBlank
    @Size(max = 1000)
    private String description;
    @NotNull
    @Positive
    @ExistById(domainClass = Category.class, idFieldName = "id", message = "Category not found")
    private Long idCategory;

    public ProductPostRequestBody(@NotBlank String name,
                                  @NotNull @PositiveOrZero Integer amount,
                                  @NotNull @Positive BigDecimal price,
                                  @Size(min = 3) @NotEmpty List<CharacteristicPostRequestBody> characteristics,
                                  @NotBlank @Size(max = 1000) String description,
                                  @NotNull @Positive Long idCategory) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.characteristics = characteristics;
        this.description = description;
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public Integer getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<CharacteristicPostRequestBody> getCharacteristics() {
        return characteristics;
    }

    public String getDescription() {
        return description;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public Product toProduct(EntityManager entityManager, Long userId) {
        Category category = Optional.ofNullable(entityManager.find(Category.class, this.idCategory))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        User user = Optional.ofNullable(entityManager.find(User.class, userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "User does not exist our you not authorized to execute this request"));

        List<Characteristic> characteristicList = characteristics.stream()
                .map(CharacteristicPostRequestBody::toCharacteristic)
                .collect(Collectors.toList());

        return new Product(this.name, this.amount, this.price, characteristicList, this.description, category, user);
    }
}

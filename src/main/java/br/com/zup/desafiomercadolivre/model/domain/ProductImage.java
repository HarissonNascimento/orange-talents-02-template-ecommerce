package br.com.zup.desafiomercadolivre.model.domain;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    @Valid
    private Product product;
    @NotBlank
    @URL
    private String link;

    public ProductImage(@NotNull @Valid Product product,
                        @NotBlank @URL String link) {
        this.product = product;
        this.link = link;
    }

    @Deprecated
    protected ProductImage() {
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getLink() {
        return link;
    }
}

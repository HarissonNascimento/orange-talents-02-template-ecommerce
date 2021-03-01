package br.com.zup.desafiomercadolivre.model.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, length = 1000)
    private String description;
    @ManyToOne
    private Product product;

    public Characteristic(@NotBlank String name,
                          @NotBlank @Size(max = 1000) String description) {
        this.name = name;
        this.description = description;
    }

    @Deprecated
    protected Characteristic() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

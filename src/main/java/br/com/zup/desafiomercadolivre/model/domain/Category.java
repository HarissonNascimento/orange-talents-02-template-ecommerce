package br.com.zup.desafiomercadolivre.model.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @ManyToOne
    private Category motherCategory;

    public Category(@NotBlank String name, @Positive Category category) {
        this.name = name;
        this.motherCategory = category;
    }

    @Deprecated
    protected Category() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getMotherCategory() {
        return motherCategory;
    }
}

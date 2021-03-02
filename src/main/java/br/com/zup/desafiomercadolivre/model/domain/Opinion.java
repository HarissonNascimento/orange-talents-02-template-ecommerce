package br.com.zup.desafiomercadolivre.model.domain;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Entity
public class Opinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer rate;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 500)
    private String description;
    @ManyToOne
    @NotNull
    @Valid
    private Product product;
    @ManyToOne
    @NotNull
    @Valid
    private User user;

    public Opinion(@Min(1) @Max(5) @NotNull Integer rate,
                   @NotBlank String title,
                   @NotBlank @Size(max = 500) String description,
                   @NotNull Product product,
                   @NotNull User user) {
        this.rate = rate;
        this.title = title;
        this.description = description;
        this.product = product;
        this.user = user;
    }

    @Deprecated
    protected Opinion() {
    }

    public Long getId() {
        return id;
    }

    public Integer getRate() {
        return rate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Product getProduct() {
        return product;
    }

    public User getUser() {
        return user;
    }
}

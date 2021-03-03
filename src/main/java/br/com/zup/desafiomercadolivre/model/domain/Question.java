package br.com.zup.desafiomercadolivre.model.domain;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Valid
    @NotNull
    @ManyToOne
    private User user;
    @Valid
    @NotNull
    @ManyToOne
    private Product product;
    @Column(nullable = false)
    private LocalDateTime instantCreation;

    public Question(@NotBlank String title,
                    @NotNull Product product,
                    @NotNull User user) {

        this.title = title;
        this.product = product;
        this.user = user;
        instantCreation = LocalDateTime.now();
    }

    @Deprecated
    protected Question() {
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getInstantCreation() {
        return instantCreation;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }

}

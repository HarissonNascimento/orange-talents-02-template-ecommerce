package br.com.zup.desafiomercadolivre.model.domain;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private BigDecimal price;
    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<Characteristic> characteristicList;
    @Column(nullable = false, length = 1000)
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private LocalDateTime registrationTime;

    public Product(@NotBlank String name,
                   @NotNull @PositiveOrZero Integer amount,
                   @NotNull @Positive BigDecimal price,
                   @Size(min = 3) @NotEmpty List<Characteristic> characteristicList,
                   @NotBlank @Size(max = 1000) String description,
                   @NotNull Category category,
                   @NotNull User user) {
        characteristicList.forEach(c -> c.setProduct(this));
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.characteristicList = characteristicList;
        this.description = description;
        this.category = category;
        this.user = user;
        this.registrationTime = LocalDateTime.now();
    }

    @Deprecated
    protected Product() {
    }

    public Long getId() {
        return id;
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

    public List<Characteristic> getCharacteristicList() {
        return characteristicList;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }
}

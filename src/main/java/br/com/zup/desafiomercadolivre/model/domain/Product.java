package br.com.zup.desafiomercadolivre.model.domain;

import br.com.zup.desafiomercadolivre.model.response.CharacteristicGetResponseBody;
import br.com.zup.desafiomercadolivre.model.response.OpinionPostResponseBody;
import br.com.zup.desafiomercadolivre.model.response.ProductDetailsGetResponseBody;
import br.com.zup.desafiomercadolivre.model.response.QuestionPostResponseBody;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingDouble;

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
    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE)
    private Set<ProductImage> images = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "product")
    private List<Opinion> opinions = new ArrayList<>();

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

    public Set<ProductImage> getImages() {
        return images;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void associateImages(Set<String> links) {

        Set<ProductImage> productImages = links.stream()
                .map(l -> new ProductImage(this, l))
                .collect(Collectors.toSet());

        this.images.addAll(productImages);
    }

    public void associateQuestion(Question question) {

        this.questions.add(question);
    }

    public ProductDetailsGetResponseBody getDetails() {
        List<String> imageLinks = this.images.stream().map(ProductImage::getLink).collect(Collectors.toList());

        List<CharacteristicGetResponseBody> characteristics = this.characteristicList.stream()
                .map(c -> new CharacteristicGetResponseBody().toCharacteristicGetResponseBody(c))
                .collect(Collectors.toList());

        List<OpinionPostResponseBody> opinionsList = this.opinions.stream()
                .map(o -> new OpinionPostResponseBody().toOpinionPostResponseBody(o))
                .collect(Collectors.toList());

        List<QuestionPostResponseBody> questionsList = this.questions.stream()
                .map(q -> new QuestionPostResponseBody().toQuestionPostResponseBody(q))
                .collect(Collectors.toList());

        Double rateAverage = this.opinions.stream().collect(averagingDouble(Opinion::getRate));

        long totalOpinions = this.opinions.size();

        return new ProductDetailsGetResponseBody(imageLinks, this.name, this.price, characteristics, this.description, rateAverage, totalOpinions, opinionsList, questionsList);
    }
}

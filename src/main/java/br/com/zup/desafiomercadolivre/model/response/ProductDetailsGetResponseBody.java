package br.com.zup.desafiomercadolivre.model.response;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsGetResponseBody {


    private List<String> imageLinks;
    private String name;
    private BigDecimal price;
    private List<CharacteristicGetResponseBody> characteristicList;
    private String description;
    private Double rateAverage;
    private long totalOpinions;
    private List<OpinionPostResponseBody> opinionsList;
    private List<QuestionPostResponseBody> questionsList;

    public ProductDetailsGetResponseBody(List<String> imageLinks,
                                         String name,
                                         BigDecimal price,
                                         List<CharacteristicGetResponseBody> characteristicList,
                                         String description,
                                         Double rateAverage,
                                         long totalOpinions,
                                         List<OpinionPostResponseBody> opinionsList,
                                         List<QuestionPostResponseBody> questionsList) {

        this.imageLinks = imageLinks;
        this.name = name;
        this.price = price;
        this.characteristicList = characteristicList;
        this.description = description;
        this.rateAverage = rateAverage;
        this.totalOpinions = totalOpinions;
        this.opinionsList = opinionsList;
        this.questionsList = questionsList;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<CharacteristicGetResponseBody> getCharacteristicList() {
        return characteristicList;
    }

    public String getDescription() {
        return description;
    }

    public Double getRateAverage() {
        return rateAverage;
    }

    public long getTotalOpinions() {
        return totalOpinions;
    }

    public List<OpinionPostResponseBody> getOpinionsList() {
        return opinionsList;
    }

    public List<QuestionPostResponseBody> getQuestionsList() {
        return questionsList;
    }
}

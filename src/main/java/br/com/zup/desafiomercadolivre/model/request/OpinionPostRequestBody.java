package br.com.zup.desafiomercadolivre.model.request;

import br.com.zup.desafiomercadolivre.model.domain.Opinion;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;

import javax.validation.constraints.*;

public class OpinionPostRequestBody {

    @Min(1)
    @Max(5)
    @NotNull
    private Integer rate;
    @NotBlank
    private String title;
    @NotBlank
    @Size(max = 500)
    private String description;

    public OpinionPostRequestBody(@Min(1) @Max(5) @NotNull Integer rate,
                                  @NotBlank String title,
                                  @NotBlank @Size(max = 500) String description) {
        this.rate = rate;
        this.title = title;
        this.description = description;
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

    public Opinion toOpinion(Product product, User user) {
        return new Opinion(this.rate, this.title, this.description, product, user);
    }
}

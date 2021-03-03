package br.com.zup.desafiomercadolivre.model.request;

import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.Question;
import br.com.zup.desafiomercadolivre.model.domain.User;

import javax.validation.constraints.NotBlank;

public class QuestionPostRequestBody {

    @NotBlank
    private String title;

    public QuestionPostRequestBody(@NotBlank String title) {
        this.title = title;
    }

    public QuestionPostRequestBody() {
    }

    public String getTitle() {
        return title;
    }

    public Question toQuestion(Product product, User user) {
        return new Question(this.title, product, user);
    }
}

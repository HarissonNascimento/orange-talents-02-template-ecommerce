package br.com.zup.desafiomercadolivre.model.response;

import br.com.zup.desafiomercadolivre.model.domain.Question;

public class QuestionPostResponseBody {
    private String title;
    private Long questionId;
    private Long userId;

    public QuestionPostResponseBody toQuestionPostResponseBody(Question question) {
        this.title = question.getTitle();
        this.questionId = question.getId();
        this.userId = question.getUser().getId();
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Long getUserId() {
        return userId;
    }
}

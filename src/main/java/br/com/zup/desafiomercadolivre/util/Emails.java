package br.com.zup.desafiomercadolivre.util;

import br.com.zup.desafiomercadolivre.model.domain.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Service
public class Emails {

    @Autowired
    private Mailer mailer;

    public void send(@NotNull @Valid Question question) {

        String productOwner = question.getProduct().getUser().getEmail();
        String questionOwner = question.getUser().getEmail();

        mailer.messenger("<html>...</html>", "Nova pergunta . . .", questionOwner, "application@email.com", productOwner);
    }
}

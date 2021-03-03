package br.com.zup.desafiomercadolivre.util;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public interface Mailer {

    void messenger(@NotBlank String body, @NotBlank String subject, @NotBlank String nameFrom, @NotBlank @Email String from, @NotBlank @Email String to);
}

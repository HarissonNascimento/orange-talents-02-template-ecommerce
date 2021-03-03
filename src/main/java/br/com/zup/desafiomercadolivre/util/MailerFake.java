package br.com.zup.desafiomercadolivre.util;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class MailerFake implements Mailer {
    @Override
    public void messenger(String body, String subject, String nameFrom, String from, String to) {
        System.out.println(body);
        System.out.println(subject);
        System.out.println(nameFrom);
        System.out.println(from);
        System.out.println(to);
    }
}

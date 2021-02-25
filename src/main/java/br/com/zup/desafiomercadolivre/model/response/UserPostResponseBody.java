package br.com.zup.desafiomercadolivre.model.response;

import br.com.zup.desafiomercadolivre.model.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UserPostResponseBody {

    @JsonFormat(pattern = "dd/MM/yyyy-HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime registrationTime;
    private String email;

    public UserPostResponseBody toUserPostResponseBody(User user) {
        this.registrationTime = user.getRegistrationTime();
        this.email = user.getEmail();
        return this;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }
}

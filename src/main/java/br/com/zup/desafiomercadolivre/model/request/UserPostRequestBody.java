package br.com.zup.desafiomercadolivre.model.request;

import br.com.zup.desafiomercadolivre.annotation.UniqueValue;
import br.com.zup.desafiomercadolivre.model.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserPostRequestBody {

    @Email
    @NotBlank
    @UniqueValue(domainClass = User.class, fieldName = "email", message = "The email already registered!")
    private final String email;
    @NotBlank
    @Size(min = 6)
    private final String password;

    public UserPostRequestBody(@Email @NotBlank String email, @NotBlank @Size(min = 6) String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User toUser() {
        return new User(this.email, this.password);
    }
}

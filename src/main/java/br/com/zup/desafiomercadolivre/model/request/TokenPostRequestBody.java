package br.com.zup.desafiomercadolivre.model.request;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TokenPostRequestBody {

    @Email
    @NotBlank
    private final String email;
    @NotBlank
    @Size(min = 6)
    private final String password;

    public TokenPostRequestBody(@Email @NotBlank String email, @NotBlank @Size(min = 6) String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
}

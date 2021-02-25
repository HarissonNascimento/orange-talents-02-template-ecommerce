package br.com.zup.desafiomercadolivre.model.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDateTime registrationTime;

    public User(@NotBlank @Email String email, @NotBlank @Size(min = 6) String password) {
        this.email = email;
        this.password = passwordManage(password);
        this.registrationTime = LocalDateTime.now();
    }

    @Deprecated
    protected User() {
    }

    private String passwordManage(String password) {
        Pattern bcryptPattern = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
        if (!bcryptPattern.matcher(password).matches()) {
            return new BCryptPasswordEncoder().encode(password);
        }
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }
}

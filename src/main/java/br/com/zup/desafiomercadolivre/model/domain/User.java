package br.com.zup.desafiomercadolivre.model.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.regex.Pattern;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDateTime registrationTime;
    private String role = "USER";

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

    public String getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return commaSeparatedStringToAuthorityList("ROLE_" + this.role);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

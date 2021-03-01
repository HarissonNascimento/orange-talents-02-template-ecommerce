package br.com.zup.desafiomercadolivre.security.service;

import br.com.zup.desafiomercadolivre.model.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    private final EntityManager entityManager;

    public AuthenticationService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Query query = entityManager.createQuery("select u from User u where u.email = :value");
        query.setParameter("value", email);

        return (User) Optional.ofNullable(query.getSingleResult())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username/password!"));
    }
}

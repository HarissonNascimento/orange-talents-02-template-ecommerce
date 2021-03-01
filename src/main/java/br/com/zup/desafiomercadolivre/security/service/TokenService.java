package br.com.zup.desafiomercadolivre.security.service;

import br.com.zup.desafiomercadolivre.model.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${desafio-mercado-livre.jwt.expiration}")
    private String expiration;

    @Value("${desafio-mercado-livre.jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API Mercado Livre")
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(body.getSubject());
    }
}

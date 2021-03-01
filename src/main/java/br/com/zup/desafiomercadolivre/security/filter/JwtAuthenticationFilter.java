package br.com.zup.desafiomercadolivre.security.filter;

import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.security.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final EntityManager entityManager;

    public JwtAuthenticationFilter(TokenService tokenService, EntityManager entityManager) {
        this.tokenService = tokenService;
        this.entityManager = entityManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(httpServletRequest);

        if (tokenService.isValidToken(token)) {
            authenticateClient(token);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void authenticateClient(String token) {
        Long userId = tokenService.getUserId(token);

        User user = Optional.ofNullable(entityManager.find(User.class, userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String recoverToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");

        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }
}

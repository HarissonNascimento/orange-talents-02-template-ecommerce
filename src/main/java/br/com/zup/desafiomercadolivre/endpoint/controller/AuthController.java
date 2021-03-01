package br.com.zup.desafiomercadolivre.endpoint.controller;

import br.com.zup.desafiomercadolivre.model.request.TokenPostRequestBody;
import br.com.zup.desafiomercadolivre.model.response.TokenPostResponseBody;
import br.com.zup.desafiomercadolivre.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/authenticate")
    public TokenPostResponseBody authenticate(@RequestBody @Valid TokenPostRequestBody tokenPostRequestBody) {
        UsernamePasswordAuthenticationToken login = tokenPostRequestBody.converter();
        Authentication authenticate = manager.authenticate(login);
        String token = tokenService.generateToken(authenticate);
        return new TokenPostResponseBody(token, "Bearer");
    }
}

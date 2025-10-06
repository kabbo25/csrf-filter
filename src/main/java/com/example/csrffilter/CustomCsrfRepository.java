package com.example.csrffilter;

import jakarta.persistence.GenerationType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.util.Optional;
import java.util.UUID;

public class CustomCsrfRepository implements CsrfTokenRepository {
    @Autowired
    private JpaTokenRepository jpaTokenRepository;
    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String generatedUUID = UUID.randomUUID().toString();
        return new DefaultCsrfToken("X-CSRF-TOKEN","_csrf",generatedUUID);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        String identity = request.getHeader("X-IDENTITY");
        Optional<Token> existingToken = jpaTokenRepository.findByIdentityType(identity);

        if(existingToken.isPresent()){
            Token existingUser = existingToken.get();
            existingUser.setToken(token.getToken());
            jpaTokenRepository.save(existingUser);
        }
        else{
            Token newToken = new Token();
            newToken.setToken(token.getToken());
            newToken.setIdentityType(identity);
            jpaTokenRepository.save(newToken);
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {

        String identity = request.getHeader("X-IDENTITY");
        Optional<Token> existingToken = jpaTokenRepository.findByIdentityType(identity);
        if(existingToken.isPresent()){
            Token token = existingToken.get();
            return new DefaultCsrfToken (
                    "X-CSRF-TOKEN",
                                "_csrf",
                    token.getToken()
            );
        }
        return null;

    }
}

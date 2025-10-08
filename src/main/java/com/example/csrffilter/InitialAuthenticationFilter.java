package com.example.csrffilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InitialAuthenticationFilter extends OncePerRequestFilter {
    private final MultiStepAuthenticationManager multiStepAuthenticationManager;

    public InitialAuthenticationFilter(MultiStepAuthenticationManager multiStepAuthenticationManager) {
        this.multiStepAuthenticationManager = multiStepAuthenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = multiStepAuthenticationManager.authenticate(usernamePasswordAuthenticationToken);
            response.setHeader("Jwt", (String) authenticate.getPrincipal());
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}

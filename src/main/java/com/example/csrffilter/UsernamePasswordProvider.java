package com.example.csrffilter;

import com.example.csrffilter.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsernamePasswordProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public UsernamePasswordProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails existingUser = customUserDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        if (passwordEncoder.matches((CharSequence) authentication.getCredentials(), existingUser.getPassword())) {
            return new PreAuthenticatedAuthenticationToken(
                    existingUser.getUsername(),
                    null,
                    List.of(new SimpleGrantedAuthority("PASSWORD_VERIFY"))
            );
        } else {
            throw new BadCredentialsException("Username or Password are invalid");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

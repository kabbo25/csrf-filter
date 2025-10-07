package com.example.csrffilter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultiStepAuthenticationManager implements AuthenticationManager {
    List<AuthenticationProvider> providerList;

    public MultiStepAuthenticationManager(UsernamePasswordProvider usernamePasswordProvider,
                                          OtpAuthenticationProvider otpAuthenticationProvider) {
        this.providerList = List.of(usernamePasswordProvider, otpAuthenticationProvider);
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Class<? extends Authentication> authenticationClass = authentication.getClass();

        for(AuthenticationProvider provider: providerList){
            if(provider.supports(authenticationClass)){
                return provider.authenticate(authentication);
            }
        }
        throw new ProviderNotFoundException("No Authentication provider found for" + authenticationClass.getName());
    }
}

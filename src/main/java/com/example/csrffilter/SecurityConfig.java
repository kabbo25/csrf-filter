package com.example.csrffilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Lazy
    @Autowired
    private InitialAuthenticationFilter initialAuthenticationFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                           MultiStepAuthenticationManager multiStepAuthenticationManager) throws Exception{
        httpSecurity
            .authorizeHttpRequests(auth->
                        auth.anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(multiStepAuthenticationManager)
                .addFilterBefore(initialAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

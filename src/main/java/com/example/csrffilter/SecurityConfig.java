package com.example.csrffilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
       requestHandler.setCsrfRequestAttributeName("_csrf");

       return http.addFilterBefore(new CsrfTokenLogger(), CsrfFilter.class)
               .authorizeHttpRequests(auth->
                       auth.anyRequest().authenticated())
               .httpBasic(Customizer.withDefaults())
               .formLogin(Customizer.withDefaults())
               .csrf(c->
                       c
                               .csrfTokenRepository(csrfTokenRepository())
                               .csrfTokenRequestHandler(requestHandler)
               )
               .build();
   }
   @Bean
   public UserDetailsService userDetailsService(){
       UserDetails user = User.withUsername("kabbo")
               .password(passwordEncoder().encode("kabbo"))
               .build();
       return new InMemoryUserDetailsManager(user);
   }
   @Bean
    public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
   }
   @Bean
    public CsrfTokenRepository csrfTokenRepository(){
       return new CustomCsrfRepository();
   }
}

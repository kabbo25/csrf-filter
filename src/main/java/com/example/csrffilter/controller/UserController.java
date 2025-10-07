package com.example.csrffilter.controller;

import com.example.csrffilter.entity.CustomUser;
import com.example.csrffilter.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody CustomUser customUser){
        try {
            customUserDetailsService.addUser(customUser);
            return ResponseEntity.status(HttpServletResponse.SC_CREATED).body("new user created");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.csrffilter.repository;

import com.example.csrffilter.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<UserDetails> findByUsername(String username);
}

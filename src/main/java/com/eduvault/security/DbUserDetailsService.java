package com.eduvault.security;

import com.eduvault.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class DbUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    public DbUserDetailsService(UserRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String normalized = (email == null) ? "" : email.trim().toLowerCase();
        var u = repo.findByEmail(normalized).orElseThrow(() -> new UsernameNotFoundException("No user"));

        String role = (u.getRole() == null || u.getRole().isBlank()) ? "USER" : u.getRole().toUpperCase();

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())            // store normalized email in DB on signup
                .password(u.getPassword())             // BCrypt hash from DB
                .roles(role)                           // ensure non-null role
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
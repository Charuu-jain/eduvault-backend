package com.eduvault.controller;

import com.eduvault.model.User;
import com.eduvault.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo; this.encoder = encoder;
    }

    public record SignupReq(String email, String password, String fullName) {}

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReq body) {
        if (body == null || body.email() == null || body.password() == null) {
            return ResponseEntity.badRequest().body("Email and password required");
        }
        if (repo.existsByEmail(body.email())) {
            return ResponseEntity.status(409).body("Email already registered");
        }
        User u = new User();
        u.setEmail(body.email().trim().toLowerCase());
        u.setPasswordHash(encoder.encode(body.password()));
        u.setFullName(body.fullName());
        repo.save(u);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails user) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok().body(new Object() {
            public final String email = user.getUsername();
        });
    }
}
package com.eduvault.controller;

import com.eduvault.model.User;
import com.eduvault.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    public AuthController(UserRepository repo, PasswordEncoder encoder, AuthenticationManager authManager) {
        this.repo = repo;
        this.encoder = encoder;
        this.authManager = authManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String email = body.get("email") == null ? null : body.get("email").trim().toLowerCase();
        String password = body.get("password");
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Email and password required");
        }
        if (repo.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        repo.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body,
                                   HttpServletRequest request, HttpServletResponse response) {
        String email = body.get("email");
        String password = body.get("password");

        String normEmail = email == null ? null : email.trim().toLowerCase();
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(normEmail, password));
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession(true);
        new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.getContext(), request, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        Map<String, String> res = new HashMap<>();
        res.put("email", principal.getName());
        return ResponseEntity.ok(res);
    }
}
package com.example.auth_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Step 1: User signup
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    // Step 2: User login → validate credentials → issue JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginDetails) {
        Optional<User> optionalUser = userRepository.findByEmail(loginDetails.getEmail());

        if (optionalUser.isPresent()) {
            User userFromDb = optionalUser.get();
            if (passwordEncoder.matches(loginDetails.getPassword(), userFromDb.getPassword())) {
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        userFromDb.getEmail(), userFromDb.getPassword(), new ArrayList<>());

                String jwt = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(new AuthResponse(jwt));
            }
        }
        return ResponseEntity.status(401).body("Error: Invalid email or password.");
    }

    // Step 3: Protected endpoint 
    @GetMapping("/secure/message")
    public ResponseEntity<String> getSecretMessage() {
        return ResponseEntity.ok("Hello from a secure endpoint!");
    }
}

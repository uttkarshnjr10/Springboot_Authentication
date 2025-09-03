package com.example.auth_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- IMPORT THIS
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails; // Add this
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    

    @Autowired
    private UserRepository userRepository;

    // --- NEW ---
    // Spring injects the PasswordEncoder bean we created in SecurityConfig.
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // --- MODIFIED ---
        // Hash the password before saving the user.
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginDetails) {
        Optional<User> optionalUser = userRepository.findByEmail(loginDetails.getEmail());

        if (optionalUser.isPresent()) {
            User userFromDb = optionalUser.get();
            if (passwordEncoder.matches(loginDetails.getPassword(), userFromDb.getPassword())) {
                // Create a UserDetails object (part of Spring Security)
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        userFromDb.getEmail(), userFromDb.getPassword(), new ArrayList<>());

                // Generate the JWT
                final String jwt = jwtUtil.generateToken(userDetails);

                // Return the JWT in the response
                return ResponseEntity.ok(new AuthResponse(jwt));
            }
        }
        return ResponseEntity.status(401).body("Error: Invalid email or password.");
    }

    @GetMapping("/secure/message")
    public ResponseEntity<String> getSecretMessage() {
    // Because of the JWT security filter we will build, this endpoint is protected.
    // Only requests with a valid JWT in the Authorization header will be allowed.
    return ResponseEntity.ok("Hello from a secure endpoint!");
   }
}
package com.example.auth_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Step 1: Config class
@Configuration
public class SecurityConfig {

    // Step 2: Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use BCrypt
        return new BCryptPasswordEncoder();
    }

    // Step 3: Security filter
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF
            .csrf(csrf -> csrf.disable())
            // Set endpoint rules
            .authorizeHttpRequests(auth -> auth
                // Allow signup/login
                .requestMatchers("/api/signup", "/api/login").permitAll()
                // Secure endpoints
                .requestMatchers("/api/secure/**").authenticated()
                // All else secure
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
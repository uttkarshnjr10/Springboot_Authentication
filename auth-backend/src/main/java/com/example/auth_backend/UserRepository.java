package com.example.auth_backend;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // step1: custom method
    // step2: find by email
    Optional<User> findByEmail(String email);
}

package com.beastxfit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.beastxfit.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Find a user by their email address
    User findByEmail(String email);
} 
package com.beastxfit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.beastxfit.model.User;
import com.beastxfit.repository.UserRepository;
import com.beastxfit.exception.FitnessException;
import jakarta.validation.Valid;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public double calculateBMI(User user) {
        if (user.getHeight() <= 0 || user.getWeight() <= 0) {
            throw new FitnessException("Height and weight must be positive values", "INVALID_MEASUREMENTS");
        }
        double heightInMeters = user.getHeight() / 100;
        double bmi = user.getWeight() / (heightInMeters * heightInMeters);
        return Math.round(bmi * 100.0) / 100.0;
    }

    public User saveUser(User user) {
        // Check if username or email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new FitnessException("Email already registered", "EMAIL_EXISTS");
        }
        // Only calculate and set BMI if height and weight are positive
        if (user.getHeight() > 0 && user.getWeight() > 0) {
            double bmi = calculateBMI(user);
            user.setBmi(bmi);
        } else {
            user.setBmi(0);
        }
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new FitnessException("Error saving user", "USER_SAVE_ERROR", e);
        }
    }
    
    public User authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new FitnessException("Invalid email or password", "INVALID_CREDENTIALS");
        }
        return user;
    }
    
    public User getUserById(String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new FitnessException("User not found", "USER_NOT_FOUND"));
    }
    
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new FitnessException("User not found with email: " + email, "USER_NOT_FOUND");
        }
        return user;
    }

    public String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi < 25) {
            return "Normal weight";
        } else if (bmi >= 25 && bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public double calculateDailyCalories(User user) {
        // Calculate BMR using Harris-Benedict equation
        double bmr;
        if (user.getGender().equalsIgnoreCase("male")) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }
        
        // Activity level multipliers
        double[] activityMultipliers = {1.2, 1.375, 1.55, 1.725, 1.9};
        int level = Math.min(Math.max(user.getActivityLevel(), 1), 5); // Ensure level is between 1 and 5
        
        return bmr * activityMultipliers[level - 1];
    }

    public User updateUserDetails(String id, User updatedUser) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new FitnessException("User not found", "USER_NOT_FOUND"));
        // Update allowed fields
        if (updatedUser.getAge() != 0) user.setAge(updatedUser.getAge());
        if (updatedUser.getHeight() != 0) user.setHeight(updatedUser.getHeight());
        if (updatedUser.getWeight() != 0) user.setWeight(updatedUser.getWeight());
        if (updatedUser.getGender() != null) user.setGender(updatedUser.getGender());
        if (updatedUser.getActivityLevel() != 0) user.setActivityLevel(updatedUser.getActivityLevel());
        if (updatedUser.getFitnessGoal() != null) user.setFitnessGoal(updatedUser.getFitnessGoal());
        // Recalculate BMI if height and weight are set
        if (user.getHeight() > 0 && user.getWeight() > 0) {
            double bmi = calculateBMI(user);
            user.setBmi(bmi);
        }
        return userRepository.save(user);
    }
} 
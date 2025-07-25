package com.beastxfit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Indexed(unique = true)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    
    private int age;
    private double height; // in centimeters
    private double weight; // in kilograms
    private String gender;
    private double bmi;
    private String fitnessGoal;
    private int activityLevel;
    
    @Indexed(unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    private List<ProgressEntry> progressHistory;
    private FitnessPlan currentPlan;

    public User() {
        this.progressHistory = new ArrayList<>();
    }

    public User(int age, double height, double weight, String gender) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.progressHistory = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public List<ProgressEntry> getProgressHistory() {
        return progressHistory;
    }

    public void setProgressHistory(List<ProgressEntry> progressHistory) {
        this.progressHistory = progressHistory;
    }
    
    public void addProgressEntry(ProgressEntry entry) {
        this.progressHistory.add(entry);
    }

    public FitnessPlan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(FitnessPlan currentPlan) {
        this.currentPlan = currentPlan;
    }
    
    // Add current stats as a progress entry
    public void trackCurrentProgress() {
        ProgressEntry entry = new ProgressEntry();
        entry.setDate(LocalDate.now());
        entry.setWeight(this.weight);
        entry.setBmi(this.bmi);
        this.progressHistory.add(entry);
    }

    // Add getters and setters for username and password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 
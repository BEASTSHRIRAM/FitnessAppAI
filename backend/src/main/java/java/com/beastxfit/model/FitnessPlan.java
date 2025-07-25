package com.beastxfit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "fitness_plans")
public class FitnessPlan {
    @Id
    private String id;
    private String goalType; // "lose", "gain", "maintain"
    private int durationWeeks;
    private List<WorkoutDay> workoutPlan;
    private List<String> dietPlan;
    private int targetCalories;
    private double targetProtein;
    private double targetCarbs;
    private double targetFats;

    public FitnessPlan() {
        this.workoutPlan = new ArrayList<>();
        this.dietPlan = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public List<WorkoutDay> getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(List<WorkoutDay> workoutPlan) {
        this.workoutPlan = workoutPlan;
    }
    
    public void addWorkoutDay(WorkoutDay workoutDay) {
        this.workoutPlan.add(workoutDay);
    }

    public List<String> getDietPlan() {
        return dietPlan;
    }

    public void setDietPlan(List<String> dietPlan) {
        this.dietPlan = dietPlan;
    }

    public int getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(int targetCalories) {
        this.targetCalories = targetCalories;
    }

    public double getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(double targetProtein) {
        this.targetProtein = targetProtein;
    }

    public double getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(double targetCarbs) {
        this.targetCarbs = targetCarbs;
    }

    public double getTargetFats() {
        return targetFats;
    }

    public void setTargetFats(double targetFats) {
        this.targetFats = targetFats;
    }
} 
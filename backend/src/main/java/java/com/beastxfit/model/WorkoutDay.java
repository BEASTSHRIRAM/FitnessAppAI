package com.beastxfit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "workout_days")
public class WorkoutDay {
    @Id
    private String id;
    private String dayName; // e.g., "Monday", "Day 1"
    private String focusArea; // e.g., "Chest", "Legs", "Cardio", "Full Body"
    private List<Exercise> exercises;
    private int restTimeSeconds; // Rest time between exercises
    private String notes;

    public WorkoutDay() {
        this.exercises = new ArrayList<>();
    }

    public WorkoutDay(String dayName, String focusArea) {
        this.dayName = dayName;
        this.focusArea = focusArea;
        this.exercises = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public void setFocusArea(String focusArea) {
        this.focusArea = focusArea;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
    
    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public int getRestTimeSeconds() {
        return restTimeSeconds;
    }

    public void setRestTimeSeconds(int restTimeSeconds) {
        this.restTimeSeconds = restTimeSeconds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
} 
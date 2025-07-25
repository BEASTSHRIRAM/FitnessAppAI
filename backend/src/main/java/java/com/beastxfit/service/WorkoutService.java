package com.beastxfit.service;

import org.springframework.stereotype.Service;
import com.beastxfit.model.WorkoutDay;
import com.beastxfit.model.Exercise;
import com.beastxfit.exception.FitnessException;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutService {

    public WorkoutDay createWorkoutDay(String dayName, String focusArea, List<Exercise> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            throw new FitnessException("Workout must contain at least one exercise", "INVALID_WORKOUT");
        }

        WorkoutDay workoutDay = new WorkoutDay(dayName, focusArea);
        workoutDay.setExercises(exercises);
        return workoutDay;
    }

    public List<WorkoutDay> createFatLossWorkoutPlan() {
        List<WorkoutDay> workoutPlan = new ArrayList<>();
        
        // Day 1: Full Body + HIIT
        List<Exercise> day1Exercises = new ArrayList<>();
        day1Exercises.add(new Exercise("Squats", "Legs", 4, 15));
        day1Exercises.add(new Exercise("Push-ups", "Chest", 3, 15));
        day1Exercises.add(new Exercise("Dumbbell Rows", "Back", 3, 12));
        day1Exercises.add(new Exercise("Lunges", "Legs", 3, 12));
        day1Exercises.add(new Exercise("HIIT Cardio", "Cardio", 1, 15));
        workoutPlan.add(createWorkoutDay("Day 1", "Full Body + HIIT", day1Exercises));
        
        // Day 2: Active Recovery
        List<Exercise> day2Exercises = new ArrayList<>();
        day2Exercises.add(new Exercise("Walking", "Cardio", 1, 30));
        day2Exercises.add(new Exercise("Stretching", "Recovery", 1, 15));
        workoutPlan.add(createWorkoutDay("Day 2", "Active Recovery", day2Exercises));
        
        // Day 3: Upper Body + Core
        List<Exercise> day3Exercises = new ArrayList<>();
        day3Exercises.add(new Exercise("Bench Press", "Chest", 3, 12));
        day3Exercises.add(new Exercise("Lateral Raises", "Shoulders", 3, 15));
        day3Exercises.add(new Exercise("Tricep Dips", "Arms", 3, 12));
        day3Exercises.add(new Exercise("Bicycle Crunches", "Core", 3, 20));
        workoutPlan.add(createWorkoutDay("Day 3", "Upper Body + Core", day3Exercises));
        
        return workoutPlan;
    }

    public List<WorkoutDay> createMuscleGainWorkoutPlan() {
        List<WorkoutDay> workoutPlan = new ArrayList<>();
        
        // Day 1: Chest & Triceps
        List<Exercise> day1Exercises = new ArrayList<>();
        day1Exercises.add(new Exercise("Bench Press", "Chest", 4, 8));
        day1Exercises.add(new Exercise("Incline Dumbbell Press", "Chest", 3, 10));
        day1Exercises.add(new Exercise("Chest Flyes", "Chest", 3, 12));
        day1Exercises.add(new Exercise("Tricep Pushdowns", "Arms", 3, 12));
        workoutPlan.add(createWorkoutDay("Day 1", "Chest & Triceps", day1Exercises));
        
        // Day 2: Back & Biceps
        List<Exercise> day2Exercises = new ArrayList<>();
        day2Exercises.add(new Exercise("Deadlifts", "Back", 4, 6));
        day2Exercises.add(new Exercise("Pull-ups", "Back", 3, 8));
        day2Exercises.add(new Exercise("Barbell Rows", "Back", 3, 10));
        day2Exercises.add(new Exercise("Barbell Curls", "Arms", 3, 12));
        workoutPlan.add(createWorkoutDay("Day 2", "Back & Biceps", day2Exercises));
        
        return workoutPlan;
    }

    public List<WorkoutDay> createMaintenanceWorkoutPlan() {
        List<WorkoutDay> workoutPlan = new ArrayList<>();
        
        // Full Body Workout
        List<Exercise> fullBodyExercises = new ArrayList<>();
        fullBodyExercises.add(new Exercise("Squats", "Legs", 3, 10));
        fullBodyExercises.add(new Exercise("Bench Press", "Chest", 3, 10));
        fullBodyExercises.add(new Exercise("Bent Over Rows", "Back", 3, 10));
        fullBodyExercises.add(new Exercise("Overhead Press", "Shoulders", 3, 10));
        fullBodyExercises.add(new Exercise("Plank", "Core", 3, 30));
        workoutPlan.add(createWorkoutDay("Full Body", "Full Body Workout", fullBodyExercises));
        
        return workoutPlan;
    }

    public List<WorkoutDay> getWorkoutPlanByGoal(String goal) {
        switch (goal.toLowerCase()) {
            case "lose":
                return createFatLossWorkoutPlan();
            case "gain":
                return createMuscleGainWorkoutPlan();
            case "maintain":
                return createMaintenanceWorkoutPlan();
            default:
                throw new FitnessException("Invalid fitness goal: " + goal, "INVALID_GOAL");
        }
    }
} 
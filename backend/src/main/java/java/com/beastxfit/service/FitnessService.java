package com.beastxfit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastxfit.model.User;
import com.beastxfit.model.FitnessPlan;
import com.beastxfit.model.WorkoutDay;
import com.beastxfit.model.Exercise;
import com.beastxfit.model.ProgressEntry;
import com.beastxfit.repository.UserRepository;
import com.beastxfit.repository.FitnessPlanRepository;
import com.beastxfit.repository.ProgressEntryRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FitnessService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FitnessPlanRepository fitnessPlanRepository;
    
    @Autowired
    private ProgressEntryRepository progressEntryRepository;

    public double calculateBMI(User user) {
        double heightInMeters = user.getHeight() / 100;
        double bmi = user.getWeight() / (heightInMeters * heightInMeters);
        return Math.round(bmi * 100.0) / 100.0;
    }

    public Map<String, Object> getFitnessReport(User user) {
        Map<String, Object> report = new HashMap<>();
        double bmi = calculateBMI(user);
        user.setBmi(bmi);
        
        String category;
        List<String> recommendations = new ArrayList<>();
        List<String> gymTips = new ArrayList<>();
        List<String> dietPlan = new ArrayList<>();
        List<String> workoutPlan = new ArrayList<>();

        // Calculate daily calorie needs (Basic BMR using Harris-Benedict equation)
        double bmr;
        if (user.getGender().equalsIgnoreCase("male")) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }
        
        // Multiply BMR by activity factor (assuming moderate activity)
        double maintenanceCalories = bmr * 1.55;

        // Determine BMI category and provide specific recommendations
        if (bmi < 18.5) {
            category = "Underweight";
            recommendations.addAll(getWeightGainTips());
            gymTips.addAll(getMuscleGainGymTips());
            dietPlan.addAll(getCalorieSurplusDiet(maintenanceCalories));
            workoutPlan.addAll(getMuscleGainWorkout());
        } else if (bmi >= 18.5 && bmi < 25) {
            category = "Normal weight";
            recommendations.addAll(getMaintainWeightTips());
            gymTips.addAll(getGeneralFitnessTips());
            dietPlan.addAll(getMaintenanceDiet(maintenanceCalories));
            workoutPlan.addAll(getBroSplitWorkout());
        } else {
            category = bmi < 30 ? "Overweight" : "Obese";
            recommendations.addAll(getWeightLossTips());
            gymTips.addAll(getFatLossGymTips());
            dietPlan.addAll(getCalorieDeficitDiet(maintenanceCalories));
            workoutPlan.addAll(getFatLossWorkout());
        }

        report.put("bmi", bmi);
        report.put("category", category);
        report.put("recommendations", recommendations);
        report.put("gymTips", gymTips);
        report.put("dietPlan", dietPlan);
        report.put("workoutPlan", workoutPlan);
        report.put("maintenanceCalories", Math.round(maintenanceCalories));

        // Track the user's current progress
        trackUserProgress(user, bmi);
        
        // If the user exists in the database, update them
        if (user.getId() != null) {
            userRepository.save(user);
        }

        return report;
    }
    
    public User saveUser(User user) {
        // Calculate and set the BMI
        double bmi = calculateBMI(user);
        user.setBmi(bmi);
        
        // Save to MongoDB
        return userRepository.save(user);
    }
    
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public FitnessPlan createPersonalizedPlan(User user, String goal, int activityLevel) {
        FitnessPlan plan = new FitnessPlan();
        plan.setGoalType(goal);
        plan.setDurationWeeks(8); // Standard 8-week program
        
        // Calculate calories based on goal
        double bmr;
        if (user.getGender().equalsIgnoreCase("male")) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }
        
        // Activity level multipliers
        double[] activityMultipliers = {1.2, 1.375, 1.55, 1.725, 1.9};
        double maintenanceCalories = bmr * activityMultipliers[activityLevel - 1];
        
        // Set calories based on goal
        if (goal.equals("lose")) {
            plan.setTargetCalories((int) (maintenanceCalories - 500));
            plan.setTargetProtein(user.getWeight() * 2.2); // 2.2g per kg
            plan.setTargetCarbs(plan.getTargetCalories() * 0.4 / 4); // 40% calories from carbs
            plan.setTargetFats(plan.getTargetCalories() * 0.25 / 9); // 25% calories from fats
            
            // Add fat loss workout plan
            createFatLossWorkoutPlan(plan);
            
        } else if (goal.equals("gain")) {
            plan.setTargetCalories((int) (maintenanceCalories + 500));
            plan.setTargetProtein(user.getWeight() * 2.0); // 2g per kg
            plan.setTargetCarbs(plan.getTargetCalories() * 0.5 / 4); // 50% calories from carbs
            plan.setTargetFats(plan.getTargetCalories() * 0.25 / 9); // 25% calories from fats
            
            // Add muscle gain workout plan
            createMuscleGainWorkoutPlan(plan);
            
        } else { // maintain
            plan.setTargetCalories((int) maintenanceCalories);
            plan.setTargetProtein(user.getWeight() * 1.8); // 1.8g per kg
            plan.setTargetCarbs(plan.getTargetCalories() * 0.45 / 4); // 45% calories from carbs
            plan.setTargetFats(plan.getTargetCalories() * 0.3 / 9); // 30% calories from fats
            
            // Add maintenance workout plan
            createMaintenanceWorkoutPlan(plan);
        }
        
        // Save the plan to MongoDB
        FitnessPlan savedPlan = fitnessPlanRepository.save(plan);
        
        // Set user's plan and save user
        user.setCurrentPlan(savedPlan);
        userRepository.save(user);
        
        return savedPlan;
    }
    
    public ProgressEntry saveProgressEntry(ProgressEntry entry, String userId) {
        // Set the user ID reference
        entry.setUserId(userId);
        
        // Save to MongoDB
        return progressEntryRepository.save(entry);
    }
    
    public List<ProgressEntry> getProgressHistoryByUserId(String userId) {
        return progressEntryRepository.findByUserId(userId);
    }
    
    public List<ProgressEntry> getProgressHistoryByDateRange(LocalDate startDate, LocalDate endDate) {
        return progressEntryRepository.findByDateBetween(startDate, endDate);
    }
    
    private void createFatLossWorkoutPlan(FitnessPlan plan) {
        // Day 1: Full Body + HIIT
        WorkoutDay day1 = new WorkoutDay("Day 1", "Full Body + HIIT");
        day1.addExercise(new Exercise("Squats", "Legs", 4, 15));
        day1.addExercise(new Exercise("Push-ups", "Chest", 3, 15));
        day1.addExercise(new Exercise("Dumbbell Rows", "Back", 3, 12));
        day1.addExercise(new Exercise("Lunges", "Legs", 3, 12));
        day1.addExercise(new Exercise("HIIT Cardio", "Cardio", 1, 15));
        plan.addWorkoutDay(day1);
        
        // Day 2: Rest or Light Cardio
        WorkoutDay day2 = new WorkoutDay("Day 2", "Rest/Light Cardio");
        day2.addExercise(new Exercise("Walking", "Cardio", 1, 30));
        plan.addWorkoutDay(day2);
        
        // Day 3: Upper Body + Core
        WorkoutDay day3 = new WorkoutDay("Day 3", "Upper Body + Core");
        day3.addExercise(new Exercise("Bench Press", "Chest", 3, 12));
        day3.addExercise(new Exercise("Lateral Raises", "Shoulders", 3, 15));
        day3.addExercise(new Exercise("Tricep Dips", "Arms", 3, 12));
        day3.addExercise(new Exercise("Bicycle Crunches", "Core", 3, 20));
        day3.addExercise(new Exercise("Plank", "Core", 3, 30));
        plan.addWorkoutDay(day3);
        
        // Day 4: Rest or Light Cardio
        WorkoutDay day4 = new WorkoutDay("Day 4", "Rest/Light Cardio");
        day4.addExercise(new Exercise("Swimming", "Cardio", 1, 30));
        plan.addWorkoutDay(day4);
        
        // Day 5: Lower Body + HIIT
        WorkoutDay day5 = new WorkoutDay("Day 5", "Lower Body + HIIT");
        day5.addExercise(new Exercise("Deadlifts", "Back/Legs", 4, 12));
        day5.addExercise(new Exercise("Leg Press", "Legs", 3, 15));
        day5.addExercise(new Exercise("Calf Raises", "Legs", 3, 20));
        day5.addExercise(new Exercise("HIIT Cardio", "Cardio", 1, 15));
        plan.addWorkoutDay(day5);
        
        // Weekend: Active Recovery
        WorkoutDay weekend = new WorkoutDay("Weekend", "Active Recovery");
        weekend.addExercise(new Exercise("Walking/Hiking", "Cardio", 1, 45));
        weekend.addExercise(new Exercise("Stretching/Yoga", "Flexibility", 1, 20));
        plan.addWorkoutDay(weekend);
    }
    
    private void createMuscleGainWorkoutPlan(FitnessPlan plan) {
        // Day 1: Chest & Triceps
        WorkoutDay day1 = new WorkoutDay("Day 1", "Chest & Triceps");
        day1.addExercise(new Exercise("Bench Press", "Chest", 4, 8));
        day1.addExercise(new Exercise("Incline Dumbbell Press", "Chest", 3, 10));
        day1.addExercise(new Exercise("Chest Flyes", "Chest", 3, 12));
        day1.addExercise(new Exercise("Tricep Pushdowns", "Arms", 3, 12));
        day1.addExercise(new Exercise("Skull Crushers", "Arms", 3, 10));
        plan.addWorkoutDay(day1);
        
        // Day 2: Back & Biceps
        WorkoutDay day2 = new WorkoutDay("Day 2", "Back & Biceps");
        day2.addExercise(new Exercise("Deadlifts", "Back", 4, 6));
        day2.addExercise(new Exercise("Pull-ups", "Back", 3, 8));
        day2.addExercise(new Exercise("Barbell Rows", "Back", 3, 10));
        day2.addExercise(new Exercise("Barbell Curls", "Arms", 3, 12));
        day2.addExercise(new Exercise("Hammer Curls", "Arms", 3, 12));
        plan.addWorkoutDay(day2);
        
        // Day 3: Rest
        WorkoutDay day3 = new WorkoutDay("Day 3", "Rest Day");
        day3.addExercise(new Exercise("Light Walking", "Recovery", 1, 20));
        day3.addExercise(new Exercise("Stretching", "Recovery", 1, 10));
        plan.addWorkoutDay(day3);
        
        // Day 4: Shoulders & Abs
        WorkoutDay day4 = new WorkoutDay("Day 4", "Shoulders & Abs");
        day4.addExercise(new Exercise("Military Press", "Shoulders", 4, 8));
        day4.addExercise(new Exercise("Lateral Raises", "Shoulders", 3, 12));
        day4.addExercise(new Exercise("Front Raises", "Shoulders", 3, 12));
        day4.addExercise(new Exercise("Hanging Leg Raises", "Core", 3, 15));
        day4.addExercise(new Exercise("Russian Twists", "Core", 3, 20));
        plan.addWorkoutDay(day4);
        
        // Day 5: Legs
        WorkoutDay day5 = new WorkoutDay("Day 5", "Legs");
        day5.addExercise(new Exercise("Squats", "Legs", 4, 8));
        day5.addExercise(new Exercise("Leg Press", "Legs", 3, 10));
        day5.addExercise(new Exercise("Romanian Deadlifts", "Legs", 3, 10));
        day5.addExercise(new Exercise("Leg Extensions", "Legs", 3, 12));
        day5.addExercise(new Exercise("Calf Raises", "Legs", 4, 15));
        plan.addWorkoutDay(day5);
        
        // Weekend: Rest & Recovery
        WorkoutDay weekend = new WorkoutDay("Weekend", "Rest & Recovery");
        weekend.addExercise(new Exercise("Light Cardio", "Recovery", 1, 20));
        weekend.addExercise(new Exercise("Foam Rolling", "Recovery", 1, 15));
        plan.addWorkoutDay(weekend);
    }
    
    private void createMaintenanceWorkoutPlan(FitnessPlan plan) {
        // Create a 3-day full body split for maintenance
        
        // Day 1: Full Body A
        WorkoutDay day1 = new WorkoutDay("Day 1", "Full Body A");
        day1.addExercise(new Exercise("Squats", "Legs", 3, 10));
        day1.addExercise(new Exercise("Bench Press", "Chest", 3, 10));
        day1.addExercise(new Exercise("Bent Over Rows", "Back", 3, 10));
        day1.addExercise(new Exercise("Overhead Press", "Shoulders", 3, 10));
        day1.addExercise(new Exercise("Plank", "Core", 3, 30));
        plan.addWorkoutDay(day1);
        
        // Day 2: Cardio & Core
        WorkoutDay day2 = new WorkoutDay("Day 2", "Cardio & Core");
        day2.addExercise(new Exercise("Running/Cycling", "Cardio", 1, 30));
        day2.addExercise(new Exercise("Crunches", "Core", 3, 15));
        day2.addExercise(new Exercise("Russian Twists", "Core", 3, 15));
        day2.addExercise(new Exercise("Mountain Climbers", "Core", 3, 20));
        plan.addWorkoutDay(day2);
        
        // Day 3: Full Body B
        WorkoutDay day3 = new WorkoutDay("Day 3", "Full Body B");
        day3.addExercise(new Exercise("Deadlifts", "Back", 3, 10));
        day3.addExercise(new Exercise("Incline Press", "Chest", 3, 10));
        day3.addExercise(new Exercise("Pull-ups", "Back", 3, 8));
        day3.addExercise(new Exercise("Lunges", "Legs", 3, 10));
        day3.addExercise(new Exercise("Lateral Raises", "Shoulders", 3, 12));
        plan.addWorkoutDay(day3);
        
        // Day 4: Rest or Light Activity
        WorkoutDay day4 = new WorkoutDay("Day 4", "Rest/Light Activity");
        day4.addExercise(new Exercise("Walking", "Recovery", 1, 30));
        day4.addExercise(new Exercise("Stretching", "Recovery", 1, 15));
        plan.addWorkoutDay(day4);
        
        // Day 5: Full Body C
        WorkoutDay day5 = new WorkoutDay("Day 5", "Full Body C");
        day5.addExercise(new Exercise("Front Squats", "Legs", 3, 10));
        day5.addExercise(new Exercise("Dips", "Chest", 3, 10));
        day5.addExercise(new Exercise("Lat Pulldowns", "Back", 3, 10));
        day5.addExercise(new Exercise("Bicep Curls", "Arms", 3, 12));
        day5.addExercise(new Exercise("Tricep Extensions", "Arms", 3, 12));
        plan.addWorkoutDay(day5);
    }
    
    private void trackUserProgress(User user, double bmi) {
        // Create a progress entry with current stats
        ProgressEntry entry = new ProgressEntry();
        entry.setDate(LocalDate.now());
        entry.setWeight(user.getWeight());
        entry.setBmi(bmi);
        
        // Add to user's progress history
        if (user.getProgressHistory() == null) {
            user.setProgressHistory(new ArrayList<>());
        }
        user.addProgressEntry(entry);
        
        // If user has an ID, save progress entry to MongoDB
        if (user.getId() != null) {
            entry.setUserId(user.getId());
            progressEntryRepository.save(entry);
        }
    }

    private List<String> getCalorieSurplusDiet(double maintenanceCalories) {
        double surplusCalories = maintenanceCalories + 500;
        List<String> diet = new ArrayList<>();
        diet.add(String.format("Target Daily Calories: %.0f calories (500 calorie surplus)", surplusCalories));
        diet.add("Meal 1 (Breakfast): Oatmeal with banana, protein shake, and peanut butter");
        diet.add("Snack 1: Greek yogurt with granola and honey");
        diet.add("Meal 2 (Lunch): Chicken breast with brown rice and avocado");
        diet.add("Snack 2: Mixed nuts and dried fruits");
        diet.add("Meal 3 (Dinner): Salmon with sweet potato and olive oil");
        diet.add("Pre-bed: Casein protein shake or cottage cheese");
        diet.add("Protein: 2g per kg of body weight");
        diet.add("Carbs: 55-60% of total calories");
        diet.add("Fats: 25-30% of total calories");
        return diet;
    }

    private List<String> getCalorieDeficitDiet(double maintenanceCalories) {
        double deficitCalories = maintenanceCalories - 500;
        List<String> diet = new ArrayList<>();
        diet.add(String.format("Target Daily Calories: %.0f calories (500 calorie deficit)", deficitCalories));
        diet.add("Meal 1 (Breakfast): Egg whites with spinach and whole grain toast");
        diet.add("Snack 1: Apple with protein shake");
        diet.add("Meal 2 (Lunch): Grilled chicken salad with light dressing");
        diet.add("Snack 2: Carrot sticks with hummus");
        diet.add("Meal 3 (Dinner): Lean fish with steamed vegetables");
        diet.add("Evening: Green tea or herbal tea");
        diet.add("Protein: 2.2g per kg of body weight");
        diet.add("Carbs: 40-45% of total calories");
        diet.add("Fats: 20-25% of total calories");
        return diet;
    }

    private List<String> getMaintenanceDiet(double maintenanceCalories) {
        List<String> diet = new ArrayList<>();
        diet.add(String.format("Target Daily Calories: %.0f calories (maintenance)", maintenanceCalories));
        diet.add("Meal 1 (Breakfast): Whole eggs with whole grain toast and fruits");
        diet.add("Snack 1: Protein smoothie with mixed berries");
        diet.add("Meal 2 (Lunch): Turkey sandwich with avocado");
        diet.add("Snack 2: Greek yogurt with nuts");
        diet.add("Meal 3 (Dinner): Lean meat with quinoa and vegetables");
        diet.add("Protein: 1.8g per kg of body weight");
        diet.add("Carbs: 45-55% of total calories");
        diet.add("Fats: 25-30% of total calories");
        return diet;
    }

    private List<String> getBroSplitWorkout() {
        List<String> workout = new ArrayList<>();
        workout.add("Monday - Chest:");
        workout.add("- Bench Press: 4 sets of 8-12 reps");
        workout.add("- Incline Dumbbell Press: 3 sets of 10-12 reps");
        workout.add("- Cable Flyes: 3 sets of 12-15 reps");
        workout.add("\nTuesday - Back:");
        workout.add("- Deadlifts: 4 sets of 6-8 reps");
        workout.add("- Pull-ups: 3 sets to failure");
        workout.add("- Barbell Rows: 3 sets of 10-12 reps");
        workout.add("\nWednesday - Shoulders:");
        workout.add("- Military Press: 4 sets of 8-12 reps");
        workout.add("- Lateral Raises: 3 sets of 12-15 reps");
        workout.add("- Face Pulls: 3 sets of 15-20 reps");
        workout.add("\nThursday - Arms:");
        workout.add("- Barbell Curls: 4 sets of 10-12 reps");
        workout.add("- Skull Crushers: 4 sets of 10-12 reps");
        workout.add("- Hammer Curls: 3 sets of 12-15 reps");
        workout.add("\nFriday - Legs:");
        workout.add("- Squats: 4 sets of 8-12 reps");
        workout.add("- Romanian Deadlifts: 3 sets of 10-12 reps");
        workout.add("- Leg Press: 3 sets of 12-15 reps");
        return workout;
    }

    private List<String> getMuscleGainWorkout() {
        List<String> workout = new ArrayList<>();
        workout.add("Full Body Workout A (Monday/Thursday):");
        workout.add("- Squats: 5 sets of 5 reps");
        workout.add("- Bench Press: 5 sets of 5 reps");
        workout.add("- Barbell Rows: 5 sets of 5 reps");
        workout.add("- Dips: 3 sets to failure");
        workout.add("\nFull Body Workout B (Tuesday/Friday):");
        workout.add("- Deadlifts: 5 sets of 5 reps");
        workout.add("- Military Press: 5 sets of 5 reps");
        workout.add("- Pull-ups: 3 sets to failure");
        workout.add("- Lunges: 3 sets of 12 reps per leg");
        workout.add("\nNotes:");
        workout.add("- Focus on progressive overload");
        workout.add("- Rest 2-3 minutes between sets");
        workout.add("- Eat in a caloric surplus");
        return workout;
    }

    private List<String> getFatLossWorkout() {
        List<String> workout = new ArrayList<>();
        workout.add("Circuit Training (3 rounds, 40 seconds work, 20 seconds rest):");
        workout.add("1. Burpees");
        workout.add("2. Mountain Climbers");
        workout.add("3. Jump Squats");
        workout.add("4. Push-ups");
        workout.add("5. High Knees");
        workout.add("\nHIIT Cardio (4 sets):");
        workout.add("- 30 seconds sprint");
        workout.add("- 30 seconds walk");
        workout.add("\nStrength Training:");
        workout.add("- Compound movements with moderate weights");
        workout.add("- Higher reps (15-20) with shorter rest periods");
        workout.add("\nSchedule:");
        workout.add("- Monday/Wednesday/Friday: Circuit Training + Strength");
        workout.add("- Tuesday/Thursday: HIIT Cardio");
        workout.add("- Weekend: Active recovery (walking, swimming)");
        return workout;
    }

    private List<String> getWeightGainTips() {
        List<String> tips = new ArrayList<>();
        tips.add("Increase your caloric intake by 300-500 calories per day");
        tips.add("Eat protein-rich foods with every meal");
        tips.add("Include healthy fats in your diet (nuts, avocados, olive oil)");
        tips.add("Consume complex carbohydrates for energy");
        tips.add("Eat frequent meals throughout the day");
        return tips;
    }

    private List<String> getWeightLossTips() {
        List<String> tips = new ArrayList<>();
        tips.add("Create a caloric deficit of 500 calories per day");
        tips.add("Increase protein intake to preserve muscle mass");
        tips.add("Eat more fiber-rich foods to feel fuller");
        tips.add("Drink plenty of water before meals");
        tips.add("Avoid processed foods and sugary drinks");
        return tips;
    }

    private List<String> getMaintainWeightTips() {
        List<String> tips = new ArrayList<>();
        tips.add("Maintain a balanced diet with proper portions");
        tips.add("Stay hydrated throughout the day");
        tips.add("Eat a variety of nutrient-rich foods");
        tips.add("Monitor your weight regularly");
        tips.add("Practice mindful eating");
        return tips;
    }

    private List<String> getMuscleGainGymTips() {
        List<String> tips = new ArrayList<>();
        tips.add("Focus on compound exercises (squats, deadlifts, bench press)");
        tips.add("Gradually increase weights in your exercises");
        tips.add("Aim for 8-12 reps per set for muscle growth");
        tips.add("Rest 1-2 minutes between sets");
        tips.add("Get adequate rest between workouts (48 hours for muscle groups)");
        return tips;
    }

    private List<String> getFatLossGymTips() {
        List<String> tips = new ArrayList<>();
        tips.add("Combine strength training with cardio");
        tips.add("Perform high-intensity interval training (HIIT)");
        tips.add("Include circuit training in your routine");
        tips.add("Keep rest periods short between exercises");
        tips.add("Focus on full-body workouts");
        return tips;
    }

    private List<String> getGeneralFitnessTips() {
        List<String> tips = new ArrayList<>();
        tips.add("Mix cardio and strength training");
        tips.add("Stay consistent with your workout routine");
        tips.add("Focus on proper form in exercises");
        tips.add("Get 7-8 hours of sleep per night");
        tips.add("Stay motivated by setting achievable goals");
        return tips;
    }
}
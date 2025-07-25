package com.beastxfit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.beastxfit.model.User;
import com.beastxfit.exception.FitnessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DietService {
    
    @Autowired
    private UserService userService;

    public Map<String, Object> createDietPlan(User user) {
        Map<String, Object> dietPlan = new HashMap<>();
        double maintenanceCalories = userService.calculateDailyCalories(user);
        
        String goal = user.getFitnessGoal();
        if (goal == null) {
            throw new FitnessException("Fitness goal not specified", "MISSING_GOAL");
        }

        switch (goal.toLowerCase()) {
            case "lose":
                return createWeightLossDiet(maintenanceCalories, user.getWeight());
            case "gain":
                return createWeightGainDiet(maintenanceCalories, user.getWeight());
            case "maintain":
                return createMaintenanceDiet(maintenanceCalories, user.getWeight());
            default:
                throw new FitnessException("Invalid fitness goal: " + goal, "INVALID_GOAL");
        }
    }

    private Map<String, Object> createWeightLossDiet(double maintenanceCalories, double weight) {
        Map<String, Object> diet = new HashMap<>();
        double targetCalories = maintenanceCalories - 500; // 500 calorie deficit
        
        diet.put("dailyCalories", Math.round(targetCalories));
        diet.put("proteinGrams", Math.round(weight * 2.2)); // 2.2g per kg
        diet.put("carbsGrams", Math.round((targetCalories * 0.4) / 4)); // 40% of calories
        diet.put("fatsGrams", Math.round((targetCalories * 0.25) / 9)); // 25% of calories
        
        List<String> meals = new ArrayList<>();
        meals.add("Breakfast: Egg whites with spinach and whole grain toast");
        meals.add("Snack 1: Apple with protein shake");
        meals.add("Lunch: Grilled chicken salad with light dressing");
        meals.add("Snack 2: Carrot sticks with hummus");
        meals.add("Dinner: Lean fish with steamed vegetables");
        diet.put("mealPlan", meals);
        
        List<String> tips = new ArrayList<>();
        tips.add("Eat protein with every meal to preserve muscle mass");
        tips.add("Focus on high-fiber foods to stay full longer");
        tips.add("Drink water before meals to reduce hunger");
        tips.add("Avoid processed foods and sugary drinks");
        diet.put("nutritionTips", tips);
        
        return diet;
    }

    private Map<String, Object> createWeightGainDiet(double maintenanceCalories, double weight) {
        Map<String, Object> diet = new HashMap<>();
        double targetCalories = maintenanceCalories + 500; // 500 calorie surplus
        
        diet.put("dailyCalories", Math.round(targetCalories));
        diet.put("proteinGrams", Math.round(weight * 2.0)); // 2g per kg
        diet.put("carbsGrams", Math.round((targetCalories * 0.5) / 4)); // 50% of calories
        diet.put("fatsGrams", Math.round((targetCalories * 0.25) / 9)); // 25% of calories
        
        List<String> meals = new ArrayList<>();
        meals.add("Breakfast: Oatmeal with banana, protein shake, and peanut butter");
        meals.add("Snack 1: Greek yogurt with granola and honey");
        meals.add("Lunch: Chicken breast with brown rice and avocado");
        meals.add("Snack 2: Mixed nuts and dried fruits");
        meals.add("Dinner: Salmon with sweet potato and olive oil");
        meals.add("Pre-bed: Casein protein shake or cottage cheese");
        diet.put("mealPlan", meals);
        
        List<String> tips = new ArrayList<>();
        tips.add("Eat every 2-3 hours to increase calorie intake");
        tips.add("Include healthy fats to boost calorie intake");
        tips.add("Drink calories through smoothies if struggling to eat enough");
        tips.add("Focus on nutrient-dense foods");
        diet.put("nutritionTips", tips);
        
        return diet;
    }

    private Map<String, Object> createMaintenanceDiet(double maintenanceCalories, double weight) {
        Map<String, Object> diet = new HashMap<>();
        
        diet.put("dailyCalories", Math.round(maintenanceCalories));
        diet.put("proteinGrams", Math.round(weight * 1.8)); // 1.8g per kg
        diet.put("carbsGrams", Math.round((maintenanceCalories * 0.45) / 4)); // 45% of calories
        diet.put("fatsGrams", Math.round((maintenanceCalories * 0.3) / 9)); // 30% of calories
        
        List<String> meals = new ArrayList<>();
        meals.add("Breakfast: Whole eggs with whole grain toast and fruits");
        meals.add("Snack 1: Protein smoothie with mixed berries");
        meals.add("Lunch: Turkey sandwich with avocado");
        meals.add("Snack 2: Greek yogurt with nuts");
        meals.add("Dinner: Lean meat with quinoa and vegetables");
        diet.put("mealPlan", meals);
        
        List<String> tips = new ArrayList<>();
        tips.add("Maintain consistent meal timing");
        tips.add("Balance macronutrients in each meal");
        tips.add("Stay hydrated throughout the day");
        tips.add("Practice portion control");
        diet.put("nutritionTips", tips);
        
        return diet;
    }
} 
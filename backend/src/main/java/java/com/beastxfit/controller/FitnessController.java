package com.beastxfit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.beastxfit.model.User;
import com.beastxfit.model.FitnessPlan;
import com.beastxfit.model.ProgressEntry;
import com.beastxfit.service.FitnessService;
import com.beastxfit.service.ProgressService;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/fitness")
@CrossOrigin(origins = "*")
public class FitnessController {

    @Autowired
    private FitnessService fitnessService;

    @Autowired
    private ProgressService progressService;

    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculateFitness(@RequestBody User user) {
        Map<String, Object> fitnessReport = fitnessService.getFitnessReport(user);
        return ResponseEntity.ok(fitnessReport);
    }
    
    @PostMapping("/plan")
    public ResponseEntity<FitnessPlan> createPersonalizedPlan(
            @RequestBody User user,
            @RequestParam String goal,
            @RequestParam int activityLevel) {
        
        FitnessPlan plan = fitnessService.createPersonalizedPlan(user, goal, activityLevel);
        return ResponseEntity.ok(plan);
    }
    
    @PostMapping("/progress/{userId}")
    public ResponseEntity<ProgressEntry> saveProgressEntry(@PathVariable String userId, @RequestBody ProgressEntry entry) {
        ProgressEntry saved = progressService.saveProgress(entry, userId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/progress/{userId}")
    public ResponseEntity<List<ProgressEntry>> getProgressHistory(@PathVariable String userId) {
        List<ProgressEntry> history = progressService.getProgressHistory(userId);
        return ResponseEntity.ok(history);
    }
}
package com.beastxfit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.beastxfit.model.User;
import com.beastxfit.model.ProgressEntry;
import com.beastxfit.repository.ProgressEntryRepository;
import com.beastxfit.exception.FitnessException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ProgressService {
    
    @Autowired
    private ProgressEntryRepository progressEntryRepository;
    
    @Autowired
    private UserService userService;

    public ProgressEntry saveProgress(ProgressEntry entry, String userId) {
        if (entry.getDate() == null) {
            entry.setDate(LocalDate.now());
        }
        
        entry.setUserId(userId);
        
        try {
            return progressEntryRepository.save(entry);
        } catch (Exception e) {
            throw new FitnessException("Error saving progress entry", "PROGRESS_SAVE_ERROR", e);
        }
    }
    
    public List<ProgressEntry> getProgressHistory(String userId) {
        try {
            return progressEntryRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new FitnessException("Error retrieving progress history", "PROGRESS_RETRIEVAL_ERROR", e);
        }
    }
    
    public List<ProgressEntry> getProgressByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new FitnessException("Start date must be before end date", "INVALID_DATE_RANGE");
        }
        
        try {
            return progressEntryRepository.findByDateBetween(startDate, endDate);
        } catch (Exception e) {
            throw new FitnessException("Error retrieving progress by date range", "PROGRESS_RETRIEVAL_ERROR", e);
        }
    }

    public Map<String, Object> analyzeProgress(String userId) {
        List<ProgressEntry> history = getProgressHistory(userId);
        if (history.isEmpty()) {
            throw new FitnessException("No progress history found", "NO_PROGRESS_DATA");
        }

        Map<String, Object> analysis = new HashMap<>();
        
        // Get the first and last entries
        ProgressEntry firstEntry = history.get(0);
        ProgressEntry lastEntry = history.get(history.size() - 1);
        
        // Calculate changes
        double weightChange = lastEntry.getWeight() - firstEntry.getWeight();
        double bmiChange = lastEntry.getBmi() - firstEntry.getBmi();
        
        // Calculate weekly averages
        double weeklyWeightChange = weightChange / 
            (firstEntry.getDate().until(lastEntry.getDate()).getDays() / 7.0);
        
        analysis.put("totalWeightChange", Math.round(weightChange * 10.0) / 10.0);
        analysis.put("totalBmiChange", Math.round(bmiChange * 10.0) / 10.0);
        analysis.put("weeklyWeightChange", Math.round(weeklyWeightChange * 10.0) / 10.0);
        analysis.put("startDate", firstEntry.getDate());
        analysis.put("endDate", lastEntry.getDate());
        analysis.put("numberOfEntries", history.size());
        
        // Add goal-based analysis
        User user = userService.getUserById(userId);
        String goal = user.getFitnessGoal();
        boolean onTrack = false;
        
        if ("lose".equalsIgnoreCase(goal)) {
            onTrack = weightChange < 0; // Weight loss is negative change
        } else if ("gain".equalsIgnoreCase(goal)) {
            onTrack = weightChange > 0; // Weight gain is positive change
        } else {
            onTrack = Math.abs(weightChange) <= 2.0; // Maintenance within 2kg
        }
        
        analysis.put("onTrack", onTrack);
        
        return analysis;
    }
} 
package com.beastxfit.controller;

import com.beastxfit.model.WorkoutLogEntry;
import com.beastxfit.service.WorkoutLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/workout-log")
@CrossOrigin(origins = "*")
public class WorkoutLogController {
    @Autowired
    private WorkoutLogService workoutLogService;

    @PostMapping("/{userId}")
    public ResponseEntity<WorkoutLogEntry> saveWorkoutLog(@PathVariable String userId, @RequestBody WorkoutLogEntry entry) {
        WorkoutLogEntry saved = workoutLogService.saveWorkoutLog(entry, userId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WorkoutLogEntry>> getWorkoutLogs(@PathVariable String userId) {
        List<WorkoutLogEntry> logs = workoutLogService.getWorkoutLogs(userId);
        return ResponseEntity.ok(logs);
    }
} 
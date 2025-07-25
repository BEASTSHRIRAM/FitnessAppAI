package com.beastxfit.service;

import com.beastxfit.model.WorkoutLogEntry;
import com.beastxfit.repository.WorkoutLogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutLogService {
    @Autowired
    private WorkoutLogEntryRepository workoutLogEntryRepository;

    public WorkoutLogEntry saveWorkoutLog(WorkoutLogEntry entry, String userId) {
        entry.setUserId(userId);
        return workoutLogEntryRepository.save(entry);
    }

    public List<WorkoutLogEntry> getWorkoutLogs(String userId) {
        return workoutLogEntryRepository.findByUserId(userId);
    }
} 
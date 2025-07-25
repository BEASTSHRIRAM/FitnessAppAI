package com.beastxfit.repository;

import com.beastxfit.model.WorkoutLogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface WorkoutLogEntryRepository extends MongoRepository<WorkoutLogEntry, String> {
    List<WorkoutLogEntry> findByUserId(String userId);
} 
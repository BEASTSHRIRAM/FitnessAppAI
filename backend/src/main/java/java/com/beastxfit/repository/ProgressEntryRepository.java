package com.beastxfit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.beastxfit.model.ProgressEntry;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProgressEntryRepository extends MongoRepository<ProgressEntry, String> {
    // Find progress entries by date range
    List<ProgressEntry> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find progress entries by user ID
    List<ProgressEntry> findByUserId(String userId);
} 
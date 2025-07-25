package com.beastxfit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.beastxfit.model.FitnessPlan;
import java.util.List;

@Repository
public interface FitnessPlanRepository extends MongoRepository<FitnessPlan, String> {
    // Find fitness plans by goal type
    List<FitnessPlan> findByGoalType(String goalType);
} 
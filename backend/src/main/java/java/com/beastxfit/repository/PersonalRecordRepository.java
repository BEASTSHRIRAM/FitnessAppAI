package com.beastxfit.repository;

import com.beastxfit.model.PersonalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PersonalRecordRepository extends MongoRepository<PersonalRecord, String> {
    List<PersonalRecord> findByUserIdOrderByDateDesc(String userId);
} 
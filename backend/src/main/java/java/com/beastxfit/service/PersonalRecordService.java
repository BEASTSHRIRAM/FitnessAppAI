package com.beastxfit.service;

import com.beastxfit.model.PersonalRecord;
import com.beastxfit.repository.PersonalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonalRecordService {
    @Autowired
    private PersonalRecordRepository personalRecordRepository;

    public PersonalRecord savePersonalRecord(PersonalRecord record, String userId) {
        record.setUserId(userId);
        return personalRecordRepository.save(record);
    }

    public List<PersonalRecord> getPersonalRecords(String userId) {
        return personalRecordRepository.findByUserIdOrderByDateDesc(userId);
    }
} 
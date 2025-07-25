package com.beastxfit.controller;

import com.beastxfit.model.PersonalRecord;
import com.beastxfit.service.PersonalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fitness/pr")
@CrossOrigin(origins = "*")
public class PersonalRecordController {
    @Autowired
    private PersonalRecordService personalRecordService;

    @PostMapping("/{userId}")
    public ResponseEntity<PersonalRecord> savePersonalRecord(@PathVariable String userId, @RequestBody PersonalRecord record) {
        PersonalRecord saved = personalRecordService.savePersonalRecord(record, userId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PersonalRecord>> getPersonalRecords(@PathVariable String userId) {
        List<PersonalRecord> records = personalRecordService.getPersonalRecords(userId);
        return ResponseEntity.ok(records);
    }
} 
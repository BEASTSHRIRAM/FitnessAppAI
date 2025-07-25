package com.beastxfit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "workout_log_entries")
public class WorkoutLogEntry {
    @Id
    private String id;
    @Indexed
    private String userId;
    private LocalDate date;
    private String workout;
    private String notes;

    public WorkoutLogEntry() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getWorkout() { return workout; }
    public void setWorkout(String workout) { this.workout = workout; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 
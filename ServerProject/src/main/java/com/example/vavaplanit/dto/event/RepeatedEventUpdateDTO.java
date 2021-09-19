package com.example.vavaplanit.dto.event;

import com.example.vavaplanit.model.EventType;

import java.time.LocalDate;
import java.time.LocalTime;

public class RepeatedEventUpdateDTO {
    private String title;
    private String location;
    private EventType type;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime alertTime;

    public RepeatedEventUpdateDTO() {
    }

    public RepeatedEventUpdateDTO(String title, String location, EventType type, String description, LocalTime startTime, LocalTime endTime, LocalTime alertTime) {
        this.title = title;
        this.location = location;
        this.type = type;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.alertTime = alertTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(LocalTime alertTime) {
        this.alertTime = alertTime;
    }
}

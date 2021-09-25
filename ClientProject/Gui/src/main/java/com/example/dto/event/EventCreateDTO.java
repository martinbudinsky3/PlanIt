package com.example.dto.event;


import com.example.dto.repetition.RepetitionCreateDTO;
import com.example.model.EventType;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventCreateDTO {
    private String title;
    private String location;
    private EventType type;
    private String description;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private LocalDate alertDate;
    private LocalTime alertTime;
    private RepetitionCreateDTO repetition;

    public EventCreateDTO() {
    }

    public EventCreateDTO(String title, String location, EventType type, String description, LocalDate startDate,
                          LocalTime startTime, LocalDate endDate, LocalTime endTime, LocalDate alertDate, LocalTime alertTime, RepetitionCreateDTO repetition) {
        this.title = title;
        this.location = location;
        this.type = type;
        this.description = description;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
        this.repetition = repetition;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(LocalDate alertDate) {
        this.alertDate = alertDate;
    }

    public LocalTime getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(LocalTime alertTime) {
        this.alertTime = alertTime;
    }

    public RepetitionCreateDTO getRepetition() {
        return repetition;
    }

    public void setRepetition(RepetitionCreateDTO repetition) {
        this.repetition = repetition;
    }

    @Override
    public String toString() {
        return "EventCreateDTO{" +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", endDate=" + endDate +
                ", endTime=" + endTime +
                ", alertDate=" + alertDate +
                ", alertTime=" + alertTime +
                ", repetition=" + repetition +
                '}';
    }
}

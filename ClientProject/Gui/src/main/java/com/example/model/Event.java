package com.example.model;

import com.example.model.repetition.Repetition;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class Event implements Serializable {
    private long id;
    private String title;
    private String location;
    private EventType type;
    private String description;
    private LocalDate startDate;
    private List<LocalDate> dates;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private LocalDate alertDate;
    private LocalTime alertTime;
    private Repetition repetition;
    private boolean exceptionInRepetition;


    public Event() {}

    public Event(String title, String location, EventType type, String description, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                 LocalTime endTime, LocalDate alertDate, LocalTime alertTime) {
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
    }

    public Event(long id, String title, String location, String description, LocalDate startDate, LocalTime startTime,
                 LocalDate endDate, LocalTime endTime, LocalDate alertDate, LocalTime alertTime) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
    }

    public Event(long id, String title, String location, EventType type, String description, LocalDate startDate,
                 List<LocalDate> dates, LocalTime startTime, LocalDate endDate, LocalTime endTime, LocalDate alertDate,
                 LocalTime alertTime, Repetition repetition) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.type = type;
        this.description = description;
        this.startDate = startDate;
        this.dates = dates;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
        this.repetition = repetition;
    }

    public Event(long id, String title, String location, EventType type, String description, LocalDate startDate, List<LocalDate> dates,
                 LocalTime startTime, LocalDate endDate, LocalTime endTime, LocalDate alertDate, LocalTime alertTime,
                 Repetition repetition, boolean exceptionInRepetition) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.type = type;
        this.description = description;
        this.startDate = startDate;
        this.dates = dates;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
        this.repetition = repetition;
        this.exceptionInRepetition = exceptionInRepetition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(List<LocalDate> dates) {
        this.dates = dates;
    }

    public Repetition getRepetition() {
        return repetition;
    }

    public void setRepetition(Repetition repetition) {
        this.repetition = repetition;
    }

    public boolean isExceptionInRepetition() {
        return exceptionInRepetition;
    }

    public void setExceptionInRepetition(boolean exceptionInRepetition) {
        this.exceptionInRepetition = exceptionInRepetition;
    }
}

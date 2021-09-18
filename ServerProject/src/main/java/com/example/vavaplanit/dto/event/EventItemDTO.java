package com.example.vavaplanit.dto.event;

import com.example.vavaplanit.model.EventType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventItemDTO {
    private long id;
    private String title;
    private EventType type;
    private LocalDate startDate;
    private List<LocalDate> dates;
    private LocalTime startTime;

    public EventItemDTO() {
    }

    public EventItemDTO(long id, String title, EventType type, LocalDate startDate, List<LocalDate> dates, LocalTime startTime) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.startDate = startDate;
        this.dates = dates;
        this.startTime = startTime;
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(List<LocalDate> dates) {
        this.dates = dates;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
}

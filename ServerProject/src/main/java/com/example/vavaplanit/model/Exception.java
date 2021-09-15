package com.example.vavaplanit.model;

import java.time.LocalDate;

public class Exception {
    private long id;
    private LocalDate date;
    private long repetitionId;
    private Long eventId;

    public Exception() {
    }

    public Exception(LocalDate date) {
        this.date = date;
    }

    public Exception(LocalDate date, long repetitionId, Long eventId) {
        this.date = date;
        this.repetitionId = repetitionId;
        this.eventId = eventId;
    }

    public Exception(long id, LocalDate date, long repetitionId) {
        this.id = id;
        this.date = date;
        this.repetitionId = repetitionId;
    }

    public Exception(LocalDate date, long repetitionId) {
        this.date = date;
        this.repetitionId = repetitionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getRepetitionId() {
        return repetitionId;
    }

    public void setRepetitionId(long repetitionId) {
        this.repetitionId = repetitionId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}

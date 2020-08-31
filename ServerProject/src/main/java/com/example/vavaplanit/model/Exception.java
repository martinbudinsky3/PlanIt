package com.example.vavaplanit.model;

import java.time.LocalDate;

public class Exception {
    private long exceptionId;
    private LocalDate date;
    private long repetitionId;
    private Integer eventId;

    public Exception() {
    }

    public Exception(LocalDate date) {
        this.date = date;
    }

    public Exception(long exceptionId, LocalDate date, long repetitionId, Integer eventId) {
        this.exceptionId = exceptionId;
        this.date = date;
        this.repetitionId = repetitionId;
        this.eventId = eventId;
    }

    public long getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(long exceptionId) {
        this.exceptionId = exceptionId;
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

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}

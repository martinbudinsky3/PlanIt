package com.example.vavaplanit.model;

import java.time.LocalDate;

public class Exception {
    private long exceptionId;
    private LocalDate date;
    private long repetitionId;

    public Exception() {
    }

    public Exception(LocalDate date) {
        this.date = date;
    }

    public Exception(LocalDate date, long repetitionId) {
        this.date = date;
        this.repetitionId = repetitionId;
    }

    public Exception(long exceptionId, LocalDate date, long repetitionId) {
        this.exceptionId = exceptionId;
        this.date = date;
        this.repetitionId = repetitionId;
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
}

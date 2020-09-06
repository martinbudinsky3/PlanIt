package com.example.vavaplanit.model;

import java.time.LocalDate;

public class Exception {
    private int exceptionId;
    private LocalDate date;
    private int repetitionId;

    public Exception() {
    }

    public Exception(LocalDate date) {
        this.date = date;
    }

    public Exception(LocalDate date, int repetitionId) {
        this.date = date;
        this.repetitionId = repetitionId;
    }

    public Exception(int exceptionId, LocalDate date, int repetitionId) {
        this.exceptionId = exceptionId;
        this.date = date;
        this.repetitionId = repetitionId;
    }

    public int getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(int exceptionId) {
        this.exceptionId = exceptionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRepetitionId() {
        return repetitionId;
    }

    public void setRepetitionId(int repetitionId) {
        this.repetitionId = repetitionId;
    }
}

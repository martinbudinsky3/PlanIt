package com.example.vavaplanit.model.repetition;

import java.time.LocalDate;
import java.util.List;

public class YearlyRepetition extends MonthlyRepetition {
    private int month;

    public YearlyRepetition() {
    }

    public YearlyRepetition(Long eventId, LocalDate start, LocalDate end, int repetitionInterval, Integer daysOfWeek,
                            Integer dayOfMonth, Integer ordinal, int month) {
        super(eventId, start, end, repetitionInterval, daysOfWeek, dayOfMonth, ordinal);
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public List<LocalDate> figureOutDates(int month, int year) {
        return null;
    }
}
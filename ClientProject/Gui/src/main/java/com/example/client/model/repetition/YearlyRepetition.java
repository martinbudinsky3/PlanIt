package com.example.client.model.repetition;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class YearlyRepetition extends MonthlyRepetition {
    private int month;

    public YearlyRepetition() {
    }

    public YearlyRepetition(int repetitionInterval, List<DayOfWeek> daysOfWeek, Integer dayOfMonth, int month) {
        super(repetitionInterval, daysOfWeek, dayOfMonth);
        this.month = month;
    }

    public YearlyRepetition(int repetitionInterval, Integer ordinal, List<DayOfWeek> daysOfWeek, int month) {
        super(repetitionInterval, ordinal, daysOfWeek);
        this.month = month;
    }

    public YearlyRepetition(Integer eventId, LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek,
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
}

package com.example.vavaplanit.model.repetition;

import java.time.LocalDate;
import java.util.List;

// TODO extends Weekly Repetition
public class MonthlyRepetition extends WeeklyRepetition {
    private int dayOfMonth;
    private int ordinal;

    public MonthlyRepetition() {
    }

    public MonthlyRepetition(int eventId, LocalDate start, LocalDate end, int repetitionInterval, int daysOfWeek,
                             int dayOfMonth, int ordinal) {
        super(eventId, start, end, repetitionInterval, daysOfWeek);
        this.dayOfMonth = dayOfMonth;
        this.ordinal = ordinal;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public List<LocalDate> figureOutDates(int month) {
        return null;
    }
}

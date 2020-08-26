package com.example.vavaplanit.model.repetition;

import java.time.LocalDate;
import java.util.List;

// TODO extends Weekly Repetition
public class MonthlyRepetition extends WeeklyRepetition {
    private Integer dayOfMonth;
    private Integer ordinal;

    public MonthlyRepetition() {
    }

    public MonthlyRepetition(int eventId, LocalDate start, LocalDate end, int repetitionInterval, Integer daysOfWeek,
                             Integer dayOfMonth, Integer ordinal) {
        super(eventId, start, end, repetitionInterval, daysOfWeek);
        this.dayOfMonth = dayOfMonth;
        this.ordinal = ordinal;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public List<LocalDate> figureOutDates(int month) {
        return null;
    }
}

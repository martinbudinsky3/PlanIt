package com.example.vavaplanit.model.repetition;

import java.time.LocalDate;
import java.util.List;

public class MonthlyRepetition extends Repetition {
    private int dayOfMonth;
    private WeekdayOfMonth weekdayOfMonth;

    public MonthlyRepetition() {
    }

    public MonthlyRepetition(int eventId, LocalDate start, LocalDate end, int repetitionInterval, int dayOfMonth,
                             WeekdayOfMonth weekdayOfMonth) {
        super(eventId, start, end, repetitionInterval);
        this.dayOfMonth = dayOfMonth;
        this.weekdayOfMonth = weekdayOfMonth;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public WeekdayOfMonth getWeekdayOfMonth() {
        return weekdayOfMonth;
    }

    public void setWeekdayOfMonth(WeekdayOfMonth weekdayOfMonth) {
        this.weekdayOfMonth = weekdayOfMonth;
    }

    @Override
    public List<LocalDate> figureOutDates(int month) {
        return null;
    }
}

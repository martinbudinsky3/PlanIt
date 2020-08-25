package com.example.vavaplanit.model.repetition;

import java.time.DayOfWeek;

public class WeekdayOfMonth {
    private int ordinal;
    private DayOfWeek dayOfWeek;

    public WeekdayOfMonth() {
    }

    public WeekdayOfMonth(int ordinal, int dayOfWeek) {
        this.ordinal = ordinal;
        setDayOfWeek(dayOfWeek);
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        // TODO extract day of week from int
    }
}

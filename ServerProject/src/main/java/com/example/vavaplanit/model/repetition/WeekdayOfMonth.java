package com.example.vavaplanit.model.repetition;

import java.time.DayOfWeek;

public class WeekdayOfMonth {
    private int ordinal;
    private DayOfWeek dayOfWeek;

    public WeekdayOfMonth() {
    }

    public WeekdayOfMonth(int ordinal, DayOfWeek dayOfWeek) {
        this.ordinal = ordinal;
        this.dayOfWeek = dayOfWeek;
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

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}

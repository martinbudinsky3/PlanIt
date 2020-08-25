package com.example.vavaplanit.model.repetition;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class WeeklyRepetition extends Repetition {
    private List<DayOfWeek> daysOfWeek;

    public WeeklyRepetition() {
    }

    public WeeklyRepetition(LocalDate start, LocalDate end, int repetitionInterval, int daysOfWeek) {
        super(start, end, repetitionInterval);
        setDaysOfWeek(daysOfWeek);
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(int daysOfWeek) {
        // TODO extract days of week from int
    }

    @Override
    public List<LocalDate> figureOutDates(int month) {
        return null;
    }
}

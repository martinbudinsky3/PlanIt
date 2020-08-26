package com.example.vavaplanit.model.repetition;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class WeeklyRepetition extends Repetition {
    private List<DayOfWeek> daysOfWeek;

    public WeeklyRepetition() {
    }

    public WeeklyRepetition(int eventId, LocalDate start, LocalDate end, int repetitionInterval, int daysOfWeek) {
        super(eventId, start, end, repetitionInterval);
        setDaysOfWeek(daysOfWeek);
    }

    public List<DayOfWeek> getDaysOfWeek() {
        // TODO return daysOfWeek as int
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

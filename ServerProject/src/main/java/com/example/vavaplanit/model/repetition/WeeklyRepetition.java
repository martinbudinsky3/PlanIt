package com.example.vavaplanit.model.repetition;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class WeeklyRepetition extends Repetition {
    private List<DayOfWeek> daysOfWeek;

    public WeeklyRepetition() {
    }

    public WeeklyRepetition(Long eventId, LocalDate start, LocalDate end, int repetitionInterval, Integer daysOfWeek) {
        super(eventId, start, end, repetitionInterval);
        setDaysOfWeek(daysOfWeek);
    }

    public Integer getDaysOfWeek() {
        // TODO return daysOfWeek as int
        //return daysOfWeek;
        return 0;
    }

    public void setDaysOfWeek(Integer daysOfWeek) {
        // TODO extract days of week from int
    }

    @Override
    public List<LocalDate> figureOutDates(int month, int year) {
        return null;
    }
}

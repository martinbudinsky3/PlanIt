package com.example.client.model.repetition;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class WeeklyRepetition extends Repetition {
    private List<DayOfWeek> daysOfWeek = new ArrayList<DayOfWeek>();

    public WeeklyRepetition() {
    }

    public WeeklyRepetition(int repetitionInterval, List<DayOfWeek> daysOfWeek) {
        super(repetitionInterval);
        this.daysOfWeek = daysOfWeek;
    }

    public WeeklyRepetition(Integer eventId, LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek) {
        super(eventId, start, end, repetitionInterval);
        this.daysOfWeek = daysOfWeek;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}

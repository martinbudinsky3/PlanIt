package com.example.vavaplanit.model.repetition;

import com.example.vavaplanit.model.Exception;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class YearlyRepetition extends MonthlyRepetition {
    private int month;

    public YearlyRepetition() {
    }

    public YearlyRepetition(Long eventId, LocalDate start, LocalDate end, int repetitionInterval,
                            Integer daysOfWeek, Integer dayOfMonth, Integer ordinal, int month) {
        super(eventId, start, end, repetitionInterval, daysOfWeek, dayOfMonth, ordinal);
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    protected boolean checkBasicCondition(int month, int year) {
        return getMonth() == month && (year - getStart().getYear()) % getRepetitionInterval() == 0;
    }
}

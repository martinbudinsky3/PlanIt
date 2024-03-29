package com.example.vavaplanit.model.repetition;

import com.example.vavaplanit.model.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YearlyRepetition extends MonthlyRepetition {
    private final Logger logger = LoggerFactory.getLogger(YearlyRepetition.class);

    private int month;

    public YearlyRepetition() {
    }

    public YearlyRepetition(long eventId, LocalDate start, LocalDate end, int repetitionInterval,
                            Integer daysOfWeek, Integer dayOfMonth, Integer ordinal, int month) {
        super(eventId, start, end, repetitionInterval, daysOfWeek, dayOfMonth, ordinal);
        this.month = month;
    }

    public YearlyRepetition(long id, long eventId, LocalDate start, LocalDate end, int repetitionInterval, Integer daysOfWeek,
                            Integer dayOfMonth, Integer ordinal, int month) {
        super(id, eventId, start, end, repetitionInterval, daysOfWeek, dayOfMonth, ordinal);
        this.month = month;
    }

    public void validateStart() {
        if(getDayOfMonth() != null && getDayOfMonth() != 0) {
            if(getStart().getDayOfMonth() != getDayOfMonth() || getStart().getMonthValue() != month) {
                findNextValidDateForDayOfMonth();
            }
        } else if(getOrdinal() != null && getOrdinal() != 0 && getDaysOfWeek() != null) {
            findNextValidDateForOrdinalAndDayOfWeek();
        }
    }

    private void findNextValidDateForDayOfMonth() {
        LocalDate newStart = LocalDate.of(getStart().getYear(), month, getDayOfMonth());
        if(getStart().isAfter(newStart)) {
            newStart = newStart.plusYears(1);
        }

        setStart(newStart);
    }

    private void findNextValidDateForOrdinalAndDayOfWeek() {
        LocalDate newStart;
        if(getOrdinal() == 5) {
            newStart = getDateOfLastWeekdayInMonth(month, getStart().getYear());
        } else {
            newStart = getDateOfNthWeekdayInMonth(month, getStart().getYear());
        }

        if(getStart().isAfter(newStart)) {
            if(getOrdinal() == 5) {
                newStart = getDateOfLastWeekdayInMonth(month, getStart().plusYears(1).getYear());
            } else {
                newStart = getDateOfNthWeekdayInMonth(month, getStart().plusYears(1).getYear());
            }
        }

        logger.debug("YearlyRepetition - new start: " + newStart);
        setStart(newStart);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YearlyRepetition that = (YearlyRepetition) o;
        return month == that.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), month);
    }
}

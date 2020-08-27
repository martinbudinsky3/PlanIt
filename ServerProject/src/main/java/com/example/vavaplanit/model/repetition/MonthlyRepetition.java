package com.example.vavaplanit.model.repetition;

import org.joda.time.DateTime;
import org.joda.time.Months;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// TODO extends Weekly Repetition
public class MonthlyRepetition extends WeeklyRepetition {
    private Integer dayOfMonth;
    private Integer ordinal;

    public MonthlyRepetition() {
    }

    public MonthlyRepetition(Long eventId, LocalDate start, LocalDate end, int repetitionInterval, Integer daysOfWeek,
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
    public List<LocalDate> figureOutDates(int month, int year) {
        if(dayOfMonth != null) {
            return figureOutDatesFromDayOfMonth(month, year);
        } else if(ordinal != null && getDaysOfWeek() != null) {
            return figureOutDatesFromOrdinalAndDayOfWeek(month, year);
        }

        return null;
    }

    private List<LocalDate> figureOutDatesFromDayOfMonth(int month, int year) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1);

        for(LocalDate date = minDate; date.isBefore(maxDate); date = date.plusDays(1)) {
            if(getMonthDiff(date) % getRepetitionInterval() != 0) {
                break;
            }

            if(date.isBefore(getStart()) || date.isAfter(getEnd())) {
                continue;
            }

        }

        return dates;
    }

    private List<LocalDate> figureOutDatesFromOrdinalAndDayOfWeek(int month, int year) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1);

        for(LocalDate date = minDate; date.isBefore(maxDate); date = date.plusDays(1)) {
            if(date.isBefore(getStart()) || date.isAfter(getEnd())) {
                continue;
            }
        }

        return dates;
    }

    private int getMonthDiff(LocalDate date) {
        DateTime startDateTime = new DateTime().withDate(getStart().getYear(), getStart().getMonthValue(), getStart().getDayOfMonth());
        DateTime dateTime = new DateTime().withDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        return Months.monthsBetween(startDateTime, dateTime).getMonths();
    }
}

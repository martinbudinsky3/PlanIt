package com.example.vavaplanit.model.repetition;

import org.joda.time.DateTime;
import org.joda.time.Months;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
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
        if(getMonthDiff(LocalDate.of(year, month, 1)) % getRepetitionInterval() != 0) {
            return new ArrayList<>();
        }

        if(dayOfMonth != null) {
            return figureOutDatesFromDayOfMonth(month, year);
        } else if(ordinal != null && getDaysOfWeek() != null) {
            return figureOutDatesFromOrdinalAndDayOfWeek(month, year);
        }

        return new ArrayList<>();
    }

    protected List<LocalDate> figureOutDatesFromDayOfMonth(int month, int year) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate lastDayOfMonth = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);

        if(dayOfMonth > lastDayOfMonth.getDayOfMonth()) {
            dates.add(lastDayOfMonth);
        } else {
            dates.add(LocalDate.of(year, month, dayOfMonth));
        }

        return dates;
    }

    protected List<LocalDate> figureOutDatesFromOrdinalAndDayOfWeek(int month, int year) {
        List<LocalDate> dates = new ArrayList<>();

        // last weekday of month
        if(ordinal == 5) {
            dates.add(getDateOfLastWeekdayInMonth(month, year));
        } else {
            dates.add(getDateOfNthWeekdayInMonth(month, year));
        }

        return dates;
    }

    private LocalDate getDateOfLastWeekdayInMonth(int month, int year) {
        DayOfWeek dayOfWeek = getDaysOfWeek().get(0);
        LocalDate date = LocalDate.of(year, month, 1).plusMonths(1);

        return date.with(TemporalAdjusters.previous(dayOfWeek));
    }

    private LocalDate getDateOfNthWeekdayInMonth(int month, int year) {
        DayOfWeek dayOfWeek = getDaysOfWeek().get(0);
        LocalDate date = LocalDate.of(year, month, 1).minusDays(1);

        date = date.plusWeeks(ordinal-1);

        return date.with(TemporalAdjusters.next(dayOfWeek));
    }

    private int getMonthDiff(LocalDate date) {
        DateTime startDateTime = new DateTime().withDate(getStart().getYear(), getStart().getMonthValue(), 1);
        DateTime dateTime = new DateTime().withDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        return Months.monthsBetween(startDateTime, dateTime).getMonths();
    }
}

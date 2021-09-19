package com.example.vavaplanit.model.repetition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = YearlyRepetition.class, name = "YearlyRepetition")
})
public class MonthlyRepetition extends WeeklyRepetition {
    Logger logger = LoggerFactory.getLogger(MonthlyRepetition.class);

    private Integer dayOfMonth;
    private Integer ordinal;

    public MonthlyRepetition() {
    }

    public MonthlyRepetition(long eventId, LocalDate start, LocalDate end, int repetitionInterval,
                             Integer daysOfWeek, Integer dayOfMonth, Integer ordinal) {
        super(eventId, start, end, repetitionInterval, daysOfWeek);
        this.dayOfMonth = dayOfMonth;
        this.ordinal = ordinal;
    }

    public MonthlyRepetition(long id, long eventId, LocalDate start, LocalDate end, int repetitionInterval, Integer daysOfWeek,
                             Integer dayOfMonth, Integer ordinal) {
        super(id, eventId, start, end, repetitionInterval, daysOfWeek);
        this.dayOfMonth = dayOfMonth;
        this.ordinal = ordinal;
    }

    public void validateStart() {
        if(dayOfMonth != null && dayOfMonth != 0) {
            if(getStart().getDayOfMonth() != dayOfMonth) {
                findNextValidDateForDayOfMonth();
            }
        } else if(ordinal != null && ordinal != 0 && getDaysOfWeek() != null) {
            findNextValidDateForOrdinalAndDayOfWeek();
        }
    }

    private void findNextValidDateForDayOfMonth() {
        LocalDate newStart;
        if(getStart().getDayOfMonth() < dayOfMonth) {
            newStart = LocalDate.of(getStart().getYear(), getStart().getMonthValue(), dayOfMonth);
        } else {
            newStart = LocalDate.of(getStart().getYear(), getStart().getMonthValue(), dayOfMonth).plusMonths(1);
        }
        setStart(newStart);
    }

    private void findNextValidDateForOrdinalAndDayOfWeek() {
        LocalDate newStart;
        if(ordinal == 5) {
            newStart = getDateOfLastWeekdayInMonth(getStart().getMonthValue(), getStart().getYear());
        } else {
            newStart = getDateOfNthWeekdayInMonth(getStart().getMonthValue(), getStart().getYear());
        }

        if(getStart().isAfter(newStart)) {
            if(ordinal == 5) {
                newStart = getDateOfLastWeekdayInMonth(getStart().plusMonths(1).getMonthValue(), getStart().plusMonths(1).getYear());
            } else {
                newStart = getDateOfNthWeekdayInMonth(getStart().plusMonths(1).getMonthValue(), getStart().plusMonths(1).getYear());
            }
        }

        setStart(newStart);
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
    public boolean checkDate(LocalDate date) {
        if(!checkBasicCondition(date.getMonthValue(), date.getYear()) || !checkBasicCondition(date)) {
            return false;
        }

        if(dayOfMonth != null && dayOfMonth != 0) {
            logger.debug("Parameter date - day of month: " + date.getDayOfMonth() + ", repetition's day of month: " + dayOfMonth);
            return checkDateByDayOfMonth(date);
        } else if(ordinal != null && ordinal != 0 && getDaysOfWeek() != null) {
            return checkDateByOrdinalAndDayOfWeek(date);
        }

        return false;
    }

    private boolean checkDateByDayOfMonth(LocalDate date) {
        LocalDate lastDayOfMonth = LocalDate.of(date.getYear(), date.getMonthValue(), 1).plusMonths(1).minusDays(1);

        if(dayOfMonth > lastDayOfMonth.getDayOfMonth()) {
            return date.equals(lastDayOfMonth);
        } else {
            logger.debug("Parameter date - day of month: " + date.getDayOfMonth() + ", repetition's day of month: " + dayOfMonth);
            return date.getDayOfMonth() == dayOfMonth;
        }
    }

    private boolean checkDateByOrdinalAndDayOfWeek(LocalDate date) {
        if(ordinal == 5) {
            return date.equals(getDateOfLastWeekdayInMonth(date.getMonthValue(), date.getYear()));
        } else {
            return date.equals(getDateOfNthWeekdayInMonth(date.getMonthValue(), date.getYear()));
        }
    }

    @Override
    public List<LocalDate> figureOutDates(int month, int year) {
        if(!checkBasicCondition(month, year)) {
            return new ArrayList<>();
        }

        if(dayOfMonth != null && dayOfMonth != 0) {
            return figureOutDatesFromDayOfMonth(month, year);
        } else if(ordinal != null && ordinal != 0 && getDaysOfWeek() != null) {
            logger.debug("ordinal: " + ordinal + ", daysOfWeek" + getDaysOfWeek());
            return figureOutDatesFromOrdinalAndDayOfWeek(month, year);
        }

        return new ArrayList<>();
    }

    protected boolean checkBasicCondition(int month, int year) {
        return getMonthDiff(LocalDate.of(year, month, 1)) % getRepetitionInterval() == 0;
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

    protected LocalDate getDateOfLastWeekdayInMonth(int month, int year) {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.with(
                TemporalAdjusters.lastInMonth( getDaysOfWeek().get(0) )
            );
    }

    protected LocalDate getDateOfNthWeekdayInMonth(int month, int year) {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.with(
                        TemporalAdjusters.dayOfWeekInMonth( ordinal , getDaysOfWeek().get(0) )
                );
    }

    private int getMonthDiff(LocalDate date) {
        DateTime startDateTime = new DateTime().withDate(getStart().getYear(), getStart().getMonthValue(), 1);
        DateTime dateTime = new DateTime().withDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        return Months.monthsBetween(startDateTime, dateTime).getMonths();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MonthlyRepetition that = (MonthlyRepetition) o;
        return dayOfMonth.equals(that.dayOfMonth) &&
                ordinal.equals(that.ordinal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dayOfMonth, ordinal);
    }
}

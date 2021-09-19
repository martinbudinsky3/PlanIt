package com.example.vavaplanit.model.repetition;

import com.fasterxml.jackson.annotation.*;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


public class WeeklyRepetition extends Repetition {
    private final int DAYS_OF_WEEK_NUMBER = 7;
    private final Map<Integer, DayOfWeek> intToDayOfWeekMap = new HashMap<Integer, DayOfWeek>(){
        {
            put(0, DayOfWeek.MONDAY);
            put(1, DayOfWeek.TUESDAY);
            put(2, DayOfWeek.WEDNESDAY);
            put(3, DayOfWeek.THURSDAY);
            put(4, DayOfWeek.FRIDAY);
            put(5, DayOfWeek.SATURDAY);
            put(6, DayOfWeek.SUNDAY);
        }
    };

    private List<DayOfWeek> daysOfWeek;

    public WeeklyRepetition() {
    }

    public WeeklyRepetition(long eventId, LocalDate start, LocalDate end, int repetitionInterval,
                            Integer daysOfWeek) {
        super(eventId, start, end, repetitionInterval);
        setDaysOfWeek(daysOfWeek);
    }

    public WeeklyRepetition(long id, long eventId, LocalDate start, LocalDate end, int repetitionInterval, Integer daysOfWeek) {
        super(id, eventId, start, end, repetitionInterval);
        setDaysOfWeek(daysOfWeek);
    }

    public void validateStart() {
        DayOfWeek startDateDayOfWeek = getStart().getDayOfWeek();
        int index = daysOfWeek.indexOf(startDateDayOfWeek);

        if(index == -1) {
            findTheEarliestDayOfWeek();
        }
    }

    private void findTheEarliestDayOfWeek() {
        LocalDate minDate = null;

        for(DayOfWeek dayOfWeek : daysOfWeek) {
            LocalDate date = getNextDayOfWeek(dayOfWeek);
            if(minDate == null || date.isBefore(minDate)) {
                minDate = date;
            }
        }

        setStart(minDate);
    }

    private LocalDate getNextDayOfWeek(DayOfWeek dayOfWeek) {
        return getStart().with(
                TemporalAdjusters.next(dayOfWeek)
        );
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    @JsonIgnore
    public Integer getDaysOfWeekInt() {
        Integer daysOfWeekInt = 0;
        for(DayOfWeek dayOfWeek : daysOfWeek) {
            int key = getKeyByValue(dayOfWeek);
            if(key >= 0) {
                daysOfWeekInt += (int) Math.pow(2, key);
            }
        }

        return daysOfWeekInt;
    }

    @JsonSetter("daysOfWeek")
    public void setDaysOfWeekFromStrings(List<String> daysOfWeek) {
        this.daysOfWeek = new ArrayList<>();
        for(String dayOfWeekName : daysOfWeek) {
            this.daysOfWeek.add(DayOfWeek.valueOf(dayOfWeekName));
        }
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public void setDaysOfWeek(Integer daysOfWeek) {
        if(daysOfWeek != null && daysOfWeek != 0) {
            this.daysOfWeek = new ArrayList<DayOfWeek>();

            for(int i = 0; i < DAYS_OF_WEEK_NUMBER; i++) {
                if(((daysOfWeek >> i) & 1) == 1) {
                    this.daysOfWeek.add(intToDayOfWeekMap.get(i));
                }
            }
        }
    }

    @Override
    public boolean checkDate(LocalDate date) {
        return checkBasicCondition(date) && getWeekDiff(date) % getRepetitionInterval() == 0 &&
                daysOfWeek.contains(date.getDayOfWeek());
    }

    @Override
    public List<LocalDate> figureOutDates(int month, int year) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1);

        for(LocalDate date = minDate; date.isBefore(maxDate); date = date.plusDays(1)) {
            if(date.isBefore(getStart()) || date.isAfter(getEnd())) {
                continue;
            }

            if(getWeekDiff(date) % getRepetitionInterval() == 0 && daysOfWeek.contains(date.getDayOfWeek())) {
                dates.add(date);
            }
        }

        return dates;
    }

    private int getKeyByValue(DayOfWeek value) {
        Set<Map.Entry<Integer, DayOfWeek>> entrySet = intToDayOfWeekMap.entrySet();
        for(Map.Entry<Integer, DayOfWeek> entry : entrySet) {
            if(entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }

        return -1;
    }

    private int getWeekDiff(LocalDate date) {
        LocalDate start;

        if(getStart().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            start = getStart();
        } else {
            start = getStart().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        }

        DateTime startDateTime = new DateTime().withDate(start.getYear(), start.getMonthValue(), start.getDayOfMonth());
        DateTime dateTime = new DateTime().withDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        return Weeks.weeksBetween(startDateTime, dateTime).getWeeks();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WeeklyRepetition that = (WeeklyRepetition) o;
        return DAYS_OF_WEEK_NUMBER == that.DAYS_OF_WEEK_NUMBER &&
                intToDayOfWeekMap.equals(that.intToDayOfWeekMap) &&
                daysOfWeek.equals(that.daysOfWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), DAYS_OF_WEEK_NUMBER, intToDayOfWeekMap, daysOfWeek);
    }
}

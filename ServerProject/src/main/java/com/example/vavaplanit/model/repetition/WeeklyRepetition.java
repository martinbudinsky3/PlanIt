package com.example.vavaplanit.model.repetition;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    };;

    private List<DayOfWeek> daysOfWeek = new ArrayList<DayOfWeek>();

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
        for(int i = 0; i < DAYS_OF_WEEK_NUMBER; i++) {
            if(((daysOfWeek >> i) & 1) == 1) {
                this.daysOfWeek.add(intToDayOfWeekMap.get(i));
            }
        }
    }

    @Override
    public List<LocalDate> figureOutDates(int month, int year) {
        return null;
    }
}

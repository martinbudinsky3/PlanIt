package com.example.client.model.repetition;

import com.fasterxml.jackson.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MonthlyRepetition.class, name = "MonthlyRepetition")
})
public class WeeklyRepetition extends Repetition {
    private List<DayOfWeek> daysOfWeek = new ArrayList<DayOfWeek>();

    public WeeklyRepetition() {
    }

    public WeeklyRepetition(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
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

    @JsonSetter("daysOfWeek")
    public void setDaysOfWeekFromStrings(List<String> daysOfWeek) {
        if(daysOfWeek != null) {
            for (String dayOfWeekName : daysOfWeek) {
                this.daysOfWeek.add(DayOfWeek.valueOf(dayOfWeekName));
            }
        }
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}

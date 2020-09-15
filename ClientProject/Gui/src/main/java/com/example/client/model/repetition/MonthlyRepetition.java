package com.example.client.model.repetition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = YearlyRepetition.class, name = "YearlyRepetition")
})
public class MonthlyRepetition extends WeeklyRepetition {
    private Integer dayOfMonth;
    private Integer ordinal;

    public MonthlyRepetition() {
    }

    public MonthlyRepetition(int repetitionInterval, List<DayOfWeek> daysOfWeek, Integer dayOfMonth) {
        super(repetitionInterval, daysOfWeek);
        this.dayOfMonth = dayOfMonth;
    }

    public MonthlyRepetition(int repetitionInterval, Integer ordinal, List<DayOfWeek> daysOfWeek) {
        super(repetitionInterval, daysOfWeek);
        this.ordinal = ordinal;
    }

    public MonthlyRepetition(Integer eventId, LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek,
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
}

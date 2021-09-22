package com.example.dto.repetition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public class YearlyRepetitionDetailDTO extends MonthlyRepetitionDetailDTO {
    private int month;

    public YearlyRepetitionDetailDTO() {
    }

    public YearlyRepetitionDetailDTO(long id, long eventId, LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek, Integer dayOfMonth, Integer ordinal, int month) {
        super(id, eventId, start, end, repetitionInterval, daysOfWeek, dayOfMonth, ordinal);
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}

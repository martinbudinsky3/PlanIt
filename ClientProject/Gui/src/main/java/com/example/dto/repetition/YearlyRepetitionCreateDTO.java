package com.example.dto.repetition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
public class YearlyRepetitionCreateDTO extends MonthlyRepetitionCreateDTO{
    private int month;

    public YearlyRepetitionCreateDTO() {
    }

    public YearlyRepetitionCreateDTO(LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek, Integer dayOfMonth, Integer ordinal, int month) {
        super(start, end, repetitionInterval, daysOfWeek, dayOfMonth, ordinal);
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}

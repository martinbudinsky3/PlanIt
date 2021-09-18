package com.example.vavaplanit.dto.repetition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = YearlyRepetitionDetailDTO.class, name = "YearlyRepetitionDetailDTO")
})
public class MonthlyRepetitionDetailDTO extends WeeklyRepetitionDetailDTO {
    private Integer dayOfMonth;
    private Integer ordinal;

    public MonthlyRepetitionDetailDTO() {
    }

    public MonthlyRepetitionDetailDTO(long id, LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek, Integer dayOfMonth, Integer ordinal) {
        super(id, start, end, repetitionInterval, daysOfWeek);
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

package com.example.vavaplanit.dto.repetition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = YearlyRepetitionCreateDTO.class, name = "YearlyRepetitionCreateDTO")
})
public class MonthlyRepetitionCreateDTO extends WeeklyRepetitionCreateDTO {
    private Integer dayOfMonth;
    private Integer ordinal;

    public MonthlyRepetitionCreateDTO() {
    }

    public MonthlyRepetitionCreateDTO(LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek, Integer dayOfMonth, Integer ordinal) {
        super(start, end, repetitionInterval, daysOfWeek);
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

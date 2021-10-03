package com.example.dto.repetition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MonthlyRepetitionCreateDTO.class, name = "MonthlyRepetitionCreateDTO")
})
public class WeeklyRepetitionCreateDTO extends RepetitionCreateDTO {
    private List<DayOfWeek> daysOfWeek;

    public WeeklyRepetitionCreateDTO() {
    }

    public WeeklyRepetitionCreateDTO(LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek) {
        super(start, end, repetitionInterval);
        this.daysOfWeek = daysOfWeek;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public String toString() {
        return "WeeklyRepetitionCreateDTO{" +
                "daysOfWeek=" + daysOfWeek +
                '}';
    }
}

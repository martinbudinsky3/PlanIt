package com.example.vavaplanit.model.dto.repetition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MonthlyRepetitionDetailDTO.class, name = "MonthlyRepetitionDetailDTO")
})
public class WeeklyRepetitionDetailDTO extends RepetitionDetailDTO {
    private List<DayOfWeek> daysOfWeek;

    public WeeklyRepetitionDetailDTO() {
    }

    public WeeklyRepetitionDetailDTO(long id, LocalDate start, LocalDate end, int repetitionInterval, List<DayOfWeek> daysOfWeek) {
        super(id, start, end, repetitionInterval);
        this.daysOfWeek = daysOfWeek;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}

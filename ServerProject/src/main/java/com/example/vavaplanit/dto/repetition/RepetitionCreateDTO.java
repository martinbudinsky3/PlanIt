package com.example.vavaplanit.dto.repetition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WeeklyRepetitionCreateDTO.class, name = "WeeklyRepetitionCreateDTO")
})
public class RepetitionCreateDTO {
    private LocalDate start;
    private LocalDate end;
    private int repetitionInterval;

    public RepetitionCreateDTO() {
    }

    public RepetitionCreateDTO(LocalDate start, LocalDate end, int repetitionInterval) {
        this.start = start;
        this.end = end;
        this.repetitionInterval = repetitionInterval;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public int getRepetitionInterval() {
        return repetitionInterval;
    }

    public void setRepetitionInterval(int repetitionInterval) {
        this.repetitionInterval = repetitionInterval;
    }
}

package com.example.vavaplanit.model.dto.repetition;

import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.repetition.WeeklyRepetition;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WeeklyRepetitionDetailDTO.class, name = "WeeklyRepetitionDetailDTO")
})
public class RepetitionDetailDTO {
    private long id;
    private LocalDate start;
    private LocalDate end;
    private int repetitionInterval;

    public RepetitionDetailDTO() {
    }

    public RepetitionDetailDTO(long id, LocalDate start, LocalDate end, int repetitionInterval) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.repetitionInterval = repetitionInterval;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

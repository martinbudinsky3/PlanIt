package com.example.client.model.repetition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WeeklyRepetition.class, name = "WeeklyRepetition")
})
public class Repetition {
    private Integer eventId;
    private LocalDate start;
    private LocalDate end;
    private int repetitionInterval;

    public Repetition() {
    }

    public Repetition(int repetitionInterval) {
        this.repetitionInterval = repetitionInterval;
    }

    public Repetition(Integer eventId, LocalDate start, LocalDate end, int repetitionInterval) {
        this.eventId = eventId;
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

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}

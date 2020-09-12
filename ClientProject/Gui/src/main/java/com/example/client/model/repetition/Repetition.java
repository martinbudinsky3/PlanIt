package com.example.client.model.repetition;

import java.time.LocalDate;

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

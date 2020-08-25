package com.example.vavaplanit.model.repetition;

import java.time.LocalDate;
import java.util.List;

public class Repetition {
    private int eventId;
    private LocalDate start;
    private LocalDate end;
    private int repetitionInterval;

    public Repetition() {
    }

    public Repetition(int eventId, LocalDate start, LocalDate end, int repetitionInterval) {
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

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public List<LocalDate> figureOutDates(int month) {
        return null;
    }
}

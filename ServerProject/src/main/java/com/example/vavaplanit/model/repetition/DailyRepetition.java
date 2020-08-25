package com.example.vavaplanit.model.repetition;

import java.time.LocalDate;
import java.util.List;

public class DailyRepetition {
    private LocalDate start;
    private LocalDate end;
    private int repetitionInterval;

    public DailyRepetition() {
    }

    public DailyRepetition(LocalDate start, LocalDate end, int repetitionInterval) {
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

    public List<LocalDate> figureOutDates(int month) {
        return null;
    }
}

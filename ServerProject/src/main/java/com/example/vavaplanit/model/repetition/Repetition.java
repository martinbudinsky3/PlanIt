package com.example.vavaplanit.model.repetition;

import com.example.vavaplanit.model.Exception;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public boolean checkDate(LocalDate date) {
        return checkBasicCondition(date) && getDayDiff(date) % repetitionInterval == 0;
    }

    protected boolean checkBasicCondition(LocalDate date) {
        return !date.isBefore(start) && !date.isAfter(end);
    }

    public List<LocalDate> figureOutDates(int month, int year) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1);

        for(LocalDate date = minDate; date.isBefore(maxDate); date = date.plusDays(1)) {
            if(date.isBefore(start) || date.isAfter(end)) {
                continue;
            }

            if(getDayDiff(date) % repetitionInterval == 0) {
                dates.add(date);
            }
        }

        return dates;
    }

    private int getDayDiff(LocalDate date) {
        DateTime startDateTime = new DateTime().withDate(start.getYear(), start.getMonthValue(), start.getDayOfMonth());
        DateTime dateTime = new DateTime().withDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        return Days.daysBetween(startDateTime, dateTime).getDays();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repetition that = (Repetition) o;
        return repetitionInterval == that.repetitionInterval &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, start, end, repetitionInterval);
    }

    @Override
    public String toString() {
        return "Repetition{" +
                "eventId=" + eventId +
                ", start=" + start +
                ", end=" + end +
                ", repetitionInterval=" + repetitionInterval +
                '}';
    }
}

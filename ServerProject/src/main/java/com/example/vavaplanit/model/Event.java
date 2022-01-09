package com.example.vavaplanit.model;


import com.example.vavaplanit.model.repetition.Repetition;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Event {
    private long id;
    private long authorId;
    private String title;
    private String location;
    private EventType type;
    private String description;
    private LocalDate startDate;
    private List<LocalDate> dates;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private LocalDate alertDate;
    private LocalTime alertTime;
    private Repetition repetition;
    private boolean exceptionInRepetition = false;

    public Event() {};

    public Event(long id, String title, EventType type, LocalDate startDate, LocalTime startTime) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public Event(long id, long authorId, String title, String location, EventType type, String description, LocalDate startDate, LocalTime startTime,
                 LocalDate endDate, LocalTime endTime, LocalDate alertDate, LocalTime alertTime) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.location = location;
        this.type = type;
        this.description = description;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(LocalDate alertDate) {
        this.alertDate = alertDate;
    }

    public LocalTime getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(LocalTime alertTime) {
        this.alertTime = alertTime;
    }

    public Repetition getRepetition() {
        return repetition;
    }

    public void setRepetition(Repetition repetition) {
        this.repetition = repetition;
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(List<LocalDate> dates) {
        this.dates = dates;
    }

    public boolean isExceptionInRepetition() {
        return exceptionInRepetition;
    }

    public void setExceptionInRepetition(boolean exceptionInRepetition) {
        this.exceptionInRepetition = exceptionInRepetition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id &&
                authorId == event.authorId &&
                exceptionInRepetition == event.exceptionInRepetition &&
                Objects.equals(title, event.title) &&
                Objects.equals(location, event.location) &&
                type == event.type &&
                Objects.equals(description, event.description) &&
                Objects.equals(startDate, event.startDate) &&
                Objects.equals(dates, event.dates) &&
                Objects.equals(startTime, event.startTime) &&
                Objects.equals(endDate, event.endDate) &&
                Objects.equals(endTime, event.endTime) &&
                Objects.equals(alertDate, event.alertDate) &&
                Objects.equals(alertTime, event.alertTime) &&
                Objects.equals(repetition, event.repetition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorId, title, location, type, description, startDate, dates, startTime, endDate, endTime,
                alertDate, alertTime, repetition, exceptionInRepetition);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", dates=" + dates +
                ", startTime=" + startTime +
                ", endDate=" + endDate +
                ", endTime=" + endTime +
                ", alertDate=" + alertDate +
                ", alertTime=" + alertTime +
                ", repetition=" + repetition +
                ", exceptionInRepetition=" + exceptionInRepetition +
                '}';
    }
}

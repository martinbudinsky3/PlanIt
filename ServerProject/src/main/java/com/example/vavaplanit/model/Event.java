package com.example.vavaplanit.model;


import com.example.vavaplanit.model.repetition.Repetition;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Event implements Serializable {
    // TODO move enum to separate class file
    public enum Type {
        FREE_TIME, WORK, SCHOOL, OTHERS;

        @Override
        public String toString() {
            switch (this) {
                case FREE_TIME:
                    return "Free time";

                case WORK:
                    return "Work";

                case SCHOOL:
                    return "School";

                case OTHERS:
                    return "Others";

                default:
                    return "";
            }
        }

        public static Type fromString(String stringType) {
            for(Type t : Type.values()) {
                if(t.toString().equalsIgnoreCase(stringType)) {
                    return t;
                }
            }

            return null;
        }
    }

    private long id;
    private long authorId;
    private String title;
    private String location;
    private Type type;
    private String description;
    private LocalDate startDate;
    private List<LocalDate> dates;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private LocalDate alertDate;
    private LocalTime alertTime;
    private Repetition repetition;

    public Event() {};

    public Event(int id, String title, Type type, LocalDate startDate, LocalTime startTime) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public Event(int id, int authorId, String title, String location, Type type, String description, LocalDate startDate, LocalTime startTime,
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id &&
                authorId == event.authorId &&
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
        return Objects.hash(id, authorId, title, location, type, description, startDate, dates, startTime, endDate,
                endTime, alertDate, alertTime, repetition);
    }
}

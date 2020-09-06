package com.example.vavaplanit.model;


import com.example.vavaplanit.model.repetition.Repetition;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    private int idEvent;
    private int idUser;
    @JsonIgnore
    private Integer exceptionId;
    private String title;
    private String location;
    private Type type;
    private String description;
    private LocalDate date;
    private List<LocalDate> dates = new ArrayList<>();
    private LocalTime starts;
    private LocalDate endsDate;
    private LocalTime ends;
    private LocalDate alertDate;
    private LocalTime alert;
    private Repetition repetition;

    public Event() {};

    public Event(int idEvent, String title, Type type, LocalDate date, LocalTime starts) {
        this.idEvent = idEvent;
        this.title = title;
        this.type = type;
        this.date = date;
        this.starts = starts;
    }

    public Event(int idEvent, Integer exceptionId, String title, String location, Type type, String description, LocalDate date, LocalTime starts,
                 LocalDate endsDate, LocalTime ends, LocalDate alertDate, LocalTime alert) {
        this.idEvent = idEvent;
        this.exceptionId = exceptionId;
        this.title = title;
        this.location = location;
        this.type = type;
        this.description = description;
        this.date = date;
        this.starts = starts;
        this.endsDate = endsDate;
        this.ends = ends;
        this.alertDate = alertDate;
        this.alert = alert;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Integer getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(Integer exceptionId) {
        this.exceptionId = exceptionId;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStarts() {
        return starts;
    }

    public void setStarts(LocalTime starts) {
        this.starts = starts;
    }

    public LocalDate getEndsDate() {
        return endsDate;
    }

    public void setEndsDate(LocalDate endsDate) {
        this.endsDate = endsDate;
    }

    public LocalTime getEnds() {
        return ends;
    }

    public void setEnds(LocalTime ends) {
        this.ends = ends;
    }

    public LocalDate getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(LocalDate alertDate) {
        this.alertDate = alertDate;
    }

    public LocalTime getAlert() {
        return alert;
    }

    public void setAlert(LocalTime alert) {
        this.alert = alert;
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
        return idEvent == event.idEvent &&
                idUser == event.idUser &&
                Objects.equals(exceptionId, event.exceptionId) &&
                Objects.equals(title, event.title) &&
                Objects.equals(location, event.location) &&
                type == event.type &&
                Objects.equals(description, event.description) &&
                Objects.equals(date, event.date) &&
                Objects.equals(dates, event.dates) &&
                Objects.equals(starts, event.starts) &&
                Objects.equals(endsDate, event.endsDate) &&
                Objects.equals(ends, event.ends) &&
                Objects.equals(alertDate, event.alertDate) &&
                Objects.equals(alert, event.alert) &&
                Objects.equals(repetition, event.repetition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvent, idUser, exceptionId, title, location, type, description, date, dates, starts, endsDate,
                ends, alertDate, alert, repetition);
    }
}

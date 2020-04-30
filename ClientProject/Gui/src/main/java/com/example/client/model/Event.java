package com.example.client.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event implements Serializable {
    private int idEvent;
    private int idUser;
    private String title;
    private String location;
    private String description;
    private LocalDate date;
    private LocalTime starts;
    private LocalTime ends;
    private LocalTime alert;

    public Event() {}

    public Event(String title, String location, String description, LocalDate date, LocalTime starts, LocalTime ends,
                 LocalTime alert, int idUser) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.date = date;
        this.starts = starts;
        this.ends = ends;
        this.alert = alert;
        this.idUser = idUser;
    }

    public Event(int idEvent, String title, String location, String description, LocalDate date, LocalTime starts,
                 LocalTime ends, LocalTime alert, int idUser) {
        this.idEvent = idEvent;
        this.title = title;
        this.location = location;
        this.description = description;
        this.date = date;
        this.starts = starts;
        this.ends = ends;
        this.alert = alert;
        this.idUser = idUser;
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

    public LocalTime getEnds() {
        return ends;
    }

    public void setEnds(LocalTime ends) {
        this.ends = ends;
    }

    public LocalTime getAlert() {
        return alert;
    }

    public void setAlert(LocalTime alert) {
        this.alert = alert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return idEvent == event.idEvent &&
                idUser == event.idUser &&
                Objects.equals(title, event.title) &&
                Objects.equals(location, event.location) &&
                Objects.equals(description, event.description) &&
                Objects.equals(date, event.date) &&
                Objects.equals(starts, event.starts) &&
                Objects.equals(ends, event.ends) &&
                Objects.equals(alert, event.alert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvent, idUser, title, location, description, date, starts, ends, alert);
    }
}

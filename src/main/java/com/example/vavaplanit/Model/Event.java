package com.example.vavaplanit.Model;


import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int idEvent;
    private String title;
    private String location;
    private LocalDate date;
    private LocalTime starts;
    private LocalTime ends;
    private LocalTime alert;

    public Event(int idEvent, String title, String location, LocalDate date, LocalTime starts, LocalTime ends, LocalTime alert) {
        this.idEvent = idEvent;
        this.title = title;
        this.location = location;
        this.date = date;
        this.starts = starts;
        this.ends = ends;
        this.alert = alert;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
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
}

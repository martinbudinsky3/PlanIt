package com.example.vavaplanit.dto.event;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventPostponeDTO {
    private long id;
    private LocalDate startDate;
    private LocalDate alertDate;
    private LocalTime alertTime;

    public EventPostponeDTO() {
    }

    public EventPostponeDTO(long id, LocalDate startDate, LocalDate alertDate, LocalTime alertTime) {
        this.id = id;
        this.startDate = startDate;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
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
}

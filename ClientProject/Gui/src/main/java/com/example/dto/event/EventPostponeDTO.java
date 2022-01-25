package com.example.dto.event;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventPostponeDTO {
    private long id;
    private LocalDate alertDate;
    private LocalTime alertTime;

    public EventPostponeDTO() {
    }

    public EventPostponeDTO(long id, LocalDate alertDate, LocalTime alertTime) {
        this.id = id;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

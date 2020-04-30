package com.example.vavaplanit.Database.Mappers;

import com.example.vavaplanit.Model.Event;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventMappers {
    public RowMapper<Event> mapEventFromDb() {
        return (resultSet, i) -> {
            int idEvent = resultSet.getInt("idevent");
            String title = resultSet.getString("title");
            String location = resultSet.getString("location");
            String description = resultSet.getString("description");
            LocalDate date = resultSet.getObject("date", LocalDate.class);
            LocalTime starts = resultSet.getObject("starts", LocalTime.class);
            LocalTime ends = resultSet.getObject("ends", LocalTime.class);
            LocalTime alerts = resultSet.getObject("alert", LocalTime.class);
            return new Event(
                    idEvent,
                    title,
                    location,
                    description,
                    date,
                    starts,
                    ends,
                    alerts
            );
        };
    }
}

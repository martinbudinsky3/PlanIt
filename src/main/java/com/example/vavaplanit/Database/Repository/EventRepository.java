package com.example.vavaplanit.Database.Repository;

import com.example.vavaplanit.Database.Mappers.EventMappers;
import com.example.vavaplanit.Database.Mappers.UserMappers;
import com.example.vavaplanit.Model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EventMappers eventMappers = new EventMappers();

    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Event> getAllByUserId(int userId){
        String sql = "SELECT * FROM planitschema.userevent ue JOIN planitschema.user u ON ue.iduser = u.iduser" +
                "JOIN planitschema.event e ON ue.idevent = e.idevent WHERE ue.iduser = " + userId;
        return jdbcTemplate.query(sql, eventMappers.mapEventFomDb());
    }
}

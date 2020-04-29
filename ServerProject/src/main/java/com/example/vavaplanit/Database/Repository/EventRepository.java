package com.example.vavaplanit.Database.Repository;

import com.example.vavaplanit.Database.Mappers.EventMappers;
import com.example.vavaplanit.Model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class EventRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EventMappers eventMappers = new EventMappers();

    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer add(Event event) {
        final String sql = "insert into planitschema.event (title, location, description, date, starts, ends, alert) " +
                "values (?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, event.getTitle());
                if(event.getLocation() != null) {
                    ps.setString(2, event.getLocation());
                } else {
                    ps.setNull(2, Types.VARCHAR);
                }
                if(event.getDescription() != null) {
                    ps.setString(3, event.getDescription());
                } else {
                    ps.setNull(3, Types.VARCHAR);
                }
                ps.setDate(4, Date.valueOf(event.getDate()));
                ps.setTime(5, Time.valueOf(event.getStarts()));
                ps.setTime(6, Time.valueOf(event.getEnds()));
                ps.setTime(7, Time.valueOf(event.getAlert()));
                return ps;
            }
        }, keyHolder);

        if(keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("idevent");
        } else {
            return null;
        }
    }

    public Integer addEventUser(int idUser, int idEvent) {
        final String sql = "insert into planitschema.userevent (iduser, idevent) values (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, idUser);
                ps.setInt(2, idEvent);

                return ps;
            }
        }, keyHolder);

        if(keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("idevent");
        } else {
            return null;
        }
    }

    public List<Event> getAllByUserId(int idUser){
        String sql = "SELECT * FROM planitschema.userevent ue JOIN planitschema.user u ON ue.iduser = u.iduser " +
                "JOIN planitschema.event e ON ue.idevent = e.idevent WHERE ue.iduser = " + idUser + ";";
        return jdbcTemplate.query(sql, eventMappers.mapEventFomDb());
    }

    public List<Event> getEventsByMonthAndUserId(int idUser, LocalDate minDate, LocalDate maxDate){
        String sql = "SELECT * FROM planitschema.userevent ue JOIN planitschema.user u ON ue.iduser = u.iduser " +
                "JOIN planitschema.event e ON ue.idevent = e.idevent WHERE ue.iduser = " + idUser + " " +
                "AND e.date >= '" + minDate + "' AND e.date <= '" + maxDate + "';";
        return jdbcTemplate.query(sql, eventMappers.mapEventFomDb());
    }

    public Event getEventByIdUserAndIdEvent(int idUser, int idEvent){
        String sql = "SELECT * FROM planitschema.userevent ue JOIN planitschema.event e ON ue.idevent = e.idevent\n" +
                "where e.idevent = '" + idEvent + "' and iduser = '" + idUser +"';";
        return jdbcTemplate.queryForObject(sql, eventMappers.mapEventFomDb());
    }
}

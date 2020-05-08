package com.example.vavaplanit.Database.Repository;

import com.example.vavaplanit.Database.Mappers.EventMappers;
import com.example.vavaplanit.Model.Event;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/** Communication with database. (queries relating to the Event object)*/
@Repository
public class EventRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EventMappers eventMappers = new EventMappers();

    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserting new event into DB
     * @param event event object*/
    public Integer add(Event event) {
        final String sql = "insert into planitschema.event (title, location, description, date, starts, ends_date, ends," +
                " alert_date, alert) values (?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, event.getTitle());
                if (event.getLocation() != null) {
                    ps.setString(2, event.getLocation());
                } else {
                    ps.setNull(2, Types.VARCHAR);
                }
                if (event.getDescription() != null) {
                    ps.setString(3, event.getDescription());
                } else {
                    ps.setNull(3, Types.VARCHAR);
                }
                ps.setDate(4, Date.valueOf(event.getDate()));
                ps.setTime(5, Time.valueOf(event.getStarts()));
                ps.setDate(6, Date.valueOf(event.getEndsDate()));
                ps.setTime(7, Time.valueOf(event.getEnds()));
                ps.setDate(8, Date.valueOf(event.getAlertDate()));
                ps.setTime(9, Time.valueOf(event.getAlert()));
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("idevent");
        } else {
            return null;
        }
    }

    /**
     * Assigning an event to a user
     * @param idUser ID of user
     * @param idEvent ID of Event*/
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

        if (keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("idevent");
        } else {
            return null;
        }
    }

    /**
     * Getting all events that belong to user and starts dates of these events are in selected year and month.
     */
    public List<Event> getEventsByMonthAndUserId(int idUser, LocalDate minDate, LocalDate maxDate) {
        String sql = "SELECT * FROM planitschema.userevent ue JOIN planitschema.user u ON ue.iduser = u.iduser " +
                "JOIN planitschema.event e ON ue.idevent = e.idevent WHERE ue.iduser = " + idUser + " " +
                "AND e.date >= '" + minDate + "' AND e.date <= '" + maxDate + "' ORDER BY e.starts;";
        return jdbcTemplate.query(sql, eventMappers.mapEventFromDb());
    }

    /**
     * Getting event by it's ID
     * @param idEvent ID of the event*/
    public Event getEvent(int idEvent) {
        try {
            String sql = "SELECT * FROM planitschema.event WHERE idevent = '" + idEvent + "';";
            return jdbcTemplate.queryForObject(sql, eventMappers.mapEventFromDb());
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Getting event by it's ID and user's ID
     * @param idUser ID of the user
     * @param idEvent ID of the event*/
    public Event getUserEvent(int idUser, int idEvent) {
        try {
            String sql = "SELECT * FROM planitschema.userevent ue JOIN planitschema.event e ON ue.idevent = e.idevent" +
                    " WHERE ue.iduser = '" + idUser + "' AND ue.idevent = '" + idEvent + "';";
            return jdbcTemplate.queryForObject(sql, eventMappers.mapEventFromDb());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Getting all events of user that have notifications set for a given time and date
     * @param idUser ID of user
     * @param date selected date
     * @param time selected time*/
    public List<Event> getEventsToAlert(int idUser, String date, String time) {
        String sql = "SELECT * FROM planitschema.userevent ue JOIN planitschema.event e ON ue.idevent = e.idevent" +
                " WHERE e.alert_date = '" + date + "' AND e.alert = '" + time + "' AND ue.iduser = " + idUser + ";";
        return jdbcTemplate.query(sql, eventMappers.mapEventFromDb());
    }

    /**
     * Update event
     * @param event event object which is going to be updated
     * @param id id of Event which is going to be updated*/
    public void update(int id, Event event) {
        String sql = "UPDATE planitschema.event SET title = ?, location = ?, date = ?, starts = ?, ends_date = ?, ends = ?," +
                " alert_date = ?, alert = ?, description = ? WHERE idevent = ?";
        jdbcTemplate.update(sql, event.getTitle(), event.getLocation(), event.getDate(), event.getStarts(), event.getEndsDate(),
                event.getEnds(), event.getAlertDate(), event.getAlert(), event.getDescription(), id);
    }

    /**
     * Delete event by user'd and event's id
     * @param idUser ID of user that wants to delete event
     * @param idEvent ID of Event which is going to be deleted*/
    public void deleteFromUserEvent(int idUser, int idEvent) {
        String sql = "DELETE FROM planitschema.userevent WHERE iduser = ? AND idevent = ?";
        jdbcTemplate.update(sql, idUser, idEvent);
    }

    /**
     * Delete event by event's id
     * @param id ID of Event which is going to be deleted*/
    public void deleteFromEvent(int id) {
        String sql = "DELETE FROM planitschema.event WHERE idevent = ?";
        jdbcTemplate.update(sql, id);
    }
}

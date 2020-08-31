package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.RepetitionMapper;
import com.example.vavaplanit.model.repetition.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class RepetitionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RepetitionMapper repetitionInfoMapper = new RepetitionMapper();

    @Autowired
    public RepetitionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long add(Repetition repetition) {
        final String sql = "insert into planitschema.repetition (event_id, starts, ends, repeat_interval, days_of_week, " +
                "day_of_month, repeat_ordinal, month, type) values (?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setLong(1, repetition.getEventId());
                ps.setDate(2, Date.valueOf(repetition.getStart()));
                ps.setDate(3, Date.valueOf(repetition.getEnd()));
                ps.setInt(4, repetition.getRepetitionInterval());
                if(repetition instanceof WeeklyRepetition && ((WeeklyRepetition) repetition).getDaysOfWeek() != null) {
                    ps.setInt(5, ((WeeklyRepetition) repetition).getDaysOfWeekInt());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }

                if(repetition instanceof MonthlyRepetition && ((MonthlyRepetition) repetition).getDayOfMonth() != null) {
                    ps.setInt(6, ((MonthlyRepetition) repetition).getDayOfMonth());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                if(repetition instanceof MonthlyRepetition && ((MonthlyRepetition) repetition).getOrdinal() != null) {
                    ps.setInt(7, ((MonthlyRepetition) repetition).getOrdinal());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }

                if(repetition instanceof YearlyRepetition) {
                    ps.setInt(8, ((YearlyRepetition) repetition).getMonth());
                } else {
                    ps.setNull(8, Types.INTEGER);
                }

                if(repetition.getClass() == Repetition.class) {
                    ps.setString(9, RepetitionType.DAILY.toString());
                }

                else if(repetition.getClass() == WeeklyRepetition.class) {
                    ps.setString(9, RepetitionType.WEEKLY.toString());
                }

                else if(repetition.getClass() == MonthlyRepetition.class) {
                    ps.setString(9, RepetitionType.MONTHLY.toString());
                }

                else {
                    ps.setString(9, RepetitionType.YEARLY.toString());
                }

                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKeys() != null) {
            return (Long) keyHolder.getKeys().get("event_id");
        } else {
            return null;
        }
    }

    public Repetition getRepetitionByEventId(int eventId) /*throws EmptyResultDataAccessException*/{
        String sql = "SELECT r.* FROM planitschema.repetition r JOIN planitschema.exception e WHERE r.event_id = " + eventId +
                " OR e.updated_event_id = " + eventId + ";";

        return jdbcTemplate.queryForObject(sql, repetitionInfoMapper);
    }
}

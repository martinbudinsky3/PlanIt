package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.ExceptionDateMapper;
import com.example.vavaplanit.model.Exception;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class ExceptionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ExceptionDateMapper exceptionDateMapper = new ExceptionDateMapper();

    public ExceptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer add(Exception exception) {
        final String sql = "insert into planitschema.exception (date, repetition_id) values (?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setDate(1, Date.valueOf(exception.getDate()));
                ps.setLong(2, exception.getRepetitionId());

                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("event_id");
        } else {
            return null;
        }
    }

    public List<Exception> getExceptionsDates(int repetitionId) {
        String sql = "SELECT date FROM planitschema.exception WHERE repetition_id = " + repetitionId + ";";

        return jdbcTemplate.query(sql, exceptionDateMapper);
    }
}

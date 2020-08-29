package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.ExceptionDateMapper;
import com.example.vavaplanit.model.Exception;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExceptionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ExceptionDateMapper exceptionDateMapper = new ExceptionDateMapper();

    public ExceptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long add(Exception exception) {
        // TODO - insert implementation

        return 1L;
    }

    public List<Exception> getExceptionsDates(long repetitionId) {
        String sql = "SELECT date FROM planitschema.exception WHERE repetition_id = " + repetitionId + ";";

        return jdbcTemplate.query(sql, exceptionDateMapper);
    }
}

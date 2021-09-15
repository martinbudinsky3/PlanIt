package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.UserMapper;
import com.example.vavaplanit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

/** Communication with database. (queries relating to the User object)*/
@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMappers = new UserMapper();

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserting new user into DB
     * @param  user User object which is going to be inserted*/
    public Long add(User user) {

        final String sql = "INSERT INTO users (firstname, lastname, username, password) " +
                    "values (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getPassword());

            return ps;
        }, keyHolder);

        if(keyHolder.getKeys() != null) {
            return (Long) keyHolder.getKeys().get("id");
        } else {
            return null;
        }
    }

    public User getUserByUsername(String username){
        try {
            String sql = "SELECT * FROM users " +
                    " where username = '" + username + "';";
            return jdbcTemplate.queryForObject(sql, userMappers.mapUserFomDb());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

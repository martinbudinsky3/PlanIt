package com.example.vavaplanit.Database.Repository;

import com.example.vavaplanit.Database.Mappers.UserMappers;
import com.example.vavaplanit.Model.Event;
import com.example.vavaplanit.Model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserMappers userMappers = new UserMappers();

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer add(User user) {
        final String sql = "insert into planitschema.user (firstname, lastname, username, userpassword) " +
                "values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getUserName());
                ps.setString(4, user.getUserPassword());

                return ps;
            }
        }, keyHolder);

        if(keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("iduser");
        } else {
            return null;
        }
    }

    public List<User> getAllUsers() {
        String sql = "" +
                "SELECT " +
                " idUser, " +
                " firstName, " +
                " lastName " +
                "FROM planitschema.user";

        return jdbcTemplate.query(sql, userMappers.mapUserFomDb());
    }

    public User getUserByUserNameAndUserPassword(String userName, String userPassword){
        String sql = "SELECT * FROM planitschema.user " +
                " where username = '" + userName + "' and userpassword = '" + userPassword + "';";
        return jdbcTemplate.queryForObject(sql, userMappers.mapUserFomDb());
    }


}

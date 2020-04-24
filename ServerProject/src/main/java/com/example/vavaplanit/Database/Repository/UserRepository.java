package com.example.vavaplanit.Database.Repository;

import com.example.vavaplanit.Database.Mappers.UserMappers;
import com.example.vavaplanit.Model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserMappers userMappers = new UserMappers();

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public List<User> getUserById(int idUser){
        String sql = "SELECT * FROM planitschema.user\n" +
                "where iduser = '" + idUser + "';";
        return jdbcTemplate.query(sql, userMappers.mapUserFomDb());
    }


}

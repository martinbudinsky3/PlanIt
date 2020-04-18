package com.example.vavaplanit.Database.Mappers;

import com.example.vavaplanit.Model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserMappers {

    public RowMapper<User> mapUserFomDb() {
        return (resultSet, i) -> {
            String idStr = resultSet.getString("idUser");
            int idUser = Integer.parseInt(idStr);

            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            return new User(
                    idUser,
                    firstName,
                    lastName
            );
        };
    }
}

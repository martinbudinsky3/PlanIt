package com.example.vavaplanit.database.mappers;

import com.example.vavaplanit.model.User;
import org.springframework.jdbc.core.RowMapper;

/** Class for mapping User object. */
public class UserMapper {

    public RowMapper<User> mapUserFomDb() {
        return (resultSet, i) -> {
            int idUser = resultSet.getInt("idUser");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String userName = resultSet.getString("userName");
            String userPassword = resultSet.getString("userPassword");

            return new User(
                    idUser,
                    firstName,
                    lastName,
                    userName,
                    userPassword
            );
        };
    }
}

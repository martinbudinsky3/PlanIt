package com.example.dto.mappers;

import com.example.dto.user.UserCreateDTO;
import com.example.model.User;
import org.modelmapper.ModelMapper;

public class UserMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public UserCreateDTO userToUserCreateDTO(User user) {
        return modelMapper.map(user, UserCreateDTO.class);
    }
}

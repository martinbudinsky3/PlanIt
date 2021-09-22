package com.example.dto.mappers;

import com.example.dto.user.UserCreateDTO;
import com.example.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOmapper {
    UserCreateDTO userToUserCreateDTO(User user);
}

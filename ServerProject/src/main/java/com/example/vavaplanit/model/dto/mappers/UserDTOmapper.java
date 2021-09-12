package com.example.vavaplanit.model.dto.mappers;

import com.example.vavaplanit.model.User;
import com.example.vavaplanit.model.dto.UserCreateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOmapper {
    UserCreateDTO userToUserCreateDTO(User user);
    User userCreateDTOToUser(UserCreateDTO userCreateDTO);
}

package com.example.vavaplanit.dto.mappers;

import com.example.vavaplanit.model.User;
import com.example.vavaplanit.dto.user.UserCreateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOmapper {
    User userCreateDTOToUser(UserCreateDTO userCreateDTO);
}

package com.example.vavaplanit.model.dto.mappers;

import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.dto.event.EventDetailDTO;
import com.example.vavaplanit.model.dto.event.EventItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventDTOmapper {
    EventItemDTO eventToEventItemDTO(Event event);
    EventDetailDTO eventToEventDetailDTO(Event event);
}

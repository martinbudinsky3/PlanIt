package com.example.dto.mappers;

import com.example.dto.event.*;
import com.example.model.Event;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.Collection;
import java.util.List;


@Mapper(componentModel = "spring")
public interface EventDTOmapper {
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<Event> eventItemsDTOsToEvents(Collection<EventItemDTO> event);
    Event eventDetailDTOToEvent(EventDetailDTO eventDetailDTO);
    EventCreateDTO eventToEventCreateDTO(Event event);
    EventUpdateDTO eventToEventUpdateDTO(Event event);
    RepeatedEventUpdateDTO eventToRepeatedEventUpdateDTO(Event event);
}

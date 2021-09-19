package com.example.vavaplanit.dto.mappers;

import com.example.vavaplanit.dto.event.*;
import com.example.vavaplanit.model.Event;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EventDTOmapper {
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<EventItemDTO> eventsToEventItemDTOs(Collection<Event> event);
    EventDetailDTO eventToEventDetailDTO(Event event);
    Event eventCreateDTOtoEvent(EventCreateDTO eventCreateDTO);
    Event eventUpdateDTOtoEvent(EventUpdateDTO eventUpdateDTO);
    Event repeatedEventUpdateDTOtoEvent(RepeatedEventUpdateDTO repeatedEventUpdateDTO);
}

package com.example.vavaplanit.dto.mappers;

import com.example.vavaplanit.dto.event.*;
import com.example.vavaplanit.model.Event;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = RepetitionDTOmapper.class)
public interface EventDTOmapper {
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<EventItemDTO> eventsToEventItemDTOs(Collection<Event> event);

    @Mapping(source = "repetition", target = "repetition", qualifiedByName = "repetitionToRepetitionDetailDTO")
    EventDetailDTO eventToEventDetailDTO(Event event);

    @Mapping(source = "repetition", target = "repetition", qualifiedByName = "repetitionCreateDTOtoRepetition")
    Event eventCreateDTOtoEvent(EventCreateDTO eventCreateDTO);

    Event eventUpdateDTOtoEvent(EventUpdateDTO eventUpdateDTO);

    Event repeatedEventUpdateDTOtoEvent(RepeatedEventUpdateDTO repeatedEventUpdateDTO);
}

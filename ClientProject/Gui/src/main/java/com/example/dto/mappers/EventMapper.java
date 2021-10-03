package com.example.dto.mappers;

import com.example.dto.event.*;
import com.example.dto.repetition.MonthlyRepetitionCreateDTO;
import com.example.dto.repetition.RepetitionCreateDTO;
import com.example.dto.repetition.WeeklyRepetitionCreateDTO;
import com.example.dto.repetition.YearlyRepetitionCreateDTO;
import com.example.model.Event;
import com.example.model.repetition.MonthlyRepetition;
import com.example.model.repetition.Repetition;
import com.example.model.repetition.WeeklyRepetition;
import com.example.model.repetition.YearlyRepetition;
import org.mapstruct.IterableMapping;
import org.mapstruct.NullValueMappingStrategy;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public EventMapper() {
    }

    public EventCreateDTO eventToEventCreateDTO(Event event) {
        EventCreateDTO eventCreateDTO = modelMapper.map(event, EventCreateDTO.class);

        Repetition repetition = event.getRepetition();
        if(repetition instanceof YearlyRepetition) {
            eventCreateDTO.setRepetition(modelMapper.map(repetition, YearlyRepetitionCreateDTO.class));
        } else if(repetition instanceof MonthlyRepetition) {
            eventCreateDTO.setRepetition(modelMapper.map(repetition, MonthlyRepetitionCreateDTO.class));
        } else if(repetition instanceof WeeklyRepetition) {
            eventCreateDTO.setRepetition(modelMapper.map(repetition, WeeklyRepetitionCreateDTO.class));
        } else {
            eventCreateDTO.setRepetition(modelMapper.map(repetition, RepetitionCreateDTO.class));
        }

        return eventCreateDTO;
    }

    public List<Event> eventItemsDTOsToEvents(List<EventItemDTO> eventItems) {
        return eventItems
                .stream()
                .map(eventItem -> modelMapper.map(eventItem, Event.class))
                .collect(Collectors.toList());
    }


    public Event eventDetailDTOToEvent(EventDetailDTO eventDetailDTO) {
        return modelMapper.map(eventDetailDTO, Event.class);
    }

    public EventUpdateDTO eventToEventUpdateDTO(Event event) {
        return modelMapper.map(event, EventUpdateDTO.class);
    }

    public RepeatedEventUpdateDTO eventToRepeatedEventUpdateDTO(Event event) {
        return modelMapper.map(event, RepeatedEventUpdateDTO.class);
    }
}

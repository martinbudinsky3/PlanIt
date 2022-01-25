package com.example.dto.mappers;

import com.example.dto.event.*;
import com.example.dto.repetition.*;
import com.example.model.Event;
import com.example.model.repetition.Repetition;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class EventDTOmapper {
    private ModelMapper modelMapper = new ModelMapper();
    private RepetitionDTOmapper repetitionMapper = new RepetitionDTOmapper();

    public EventDTOmapper() {
    }

    public EventCreateDTO eventToEventCreateDTO(Event event) {
        EventCreateDTO eventCreateDTO = modelMapper.map(event, EventCreateDTO.class);
        if(event.getRepetition() != null) {
            RepetitionCreateDTO repetitionCreateDTO = repetitionMapper.repetitionToRepetitionCreateDTO(event.getRepetition());
            eventCreateDTO.setRepetition(repetitionCreateDTO);
        }

        return eventCreateDTO;
    }

    public List<Event> eventItemsDTOsToEvents(List<EventItemDTO> eventItems) {
        return eventItems
                .stream()
                .map(eventItem -> modelMapper.map(eventItem, Event.class))
                .collect(Collectors.toList());
    }


    public List<Event> eventAlertDTOsToEvents(List<EventAlertDTO> eventAlertDTOS) {
        return eventAlertDTOS
                .stream()
                .map(eventAlertDTO -> modelMapper.map(eventAlertDTO, Event.class))
                .collect(Collectors.toList());
    }


    public Event eventDetailDTOToEvent(EventDetailDTO eventDetailDTO) {
        Event event = modelMapper.map(eventDetailDTO, Event.class);
        if(eventDetailDTO.getRepetition() != null) {
            Repetition repetition = repetitionMapper.repetitionDetailDTOtoRepetition(eventDetailDTO.getRepetition());
            event.setRepetition(repetition);
        }

        return event;
    }

    public EventUpdateDTO eventToEventUpdateDTO(Event event) {
        return modelMapper.map(event, EventUpdateDTO.class);
    }

    public RepeatedEventUpdateDTO eventToRepeatedEventUpdateDTO(Event event) {
        return modelMapper.map(event, RepeatedEventUpdateDTO.class);
    }

    public EventPostponeDTO eventToEventPostponeDTO(Event event) {
        return modelMapper.map(event, EventPostponeDTO.class);
    }
}

package com.example.vavaplanit.Database.Service;

import com.example.vavaplanit.Database.Repository.EventRepository;
import com.example.vavaplanit.Model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllByUserId(int userId){
        return this.eventRepository.getAllByUserId(userId);
    }
}

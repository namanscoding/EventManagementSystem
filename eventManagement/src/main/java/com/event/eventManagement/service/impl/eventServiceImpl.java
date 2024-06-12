package com.event.eventManagement.service.impl;

import com.event.eventManagement.model.events;
import com.event.eventManagement.repository.eventRepository;
import com.event.eventManagement.service.eventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class eventServiceImpl implements eventService {

    @Autowired
    private eventRepository eventRepositoryObj;
    @Override
    public ResponseEntity<Object> addEvent(events eventsObj) {
        try
        {
            events addobj = new events();
            addobj.setEventName(eventsObj.getEventName());
            addobj.setEventDate(eventsObj.getEventDate());
            addobj.setEventLocation(eventsObj.getEventLocation());
            addobj.setRegistrationDetails(eventsObj.getRegistrationDetails());

            eventRepositoryObj.save(addobj);
            return ResponseEntity.status(200).body("Event added successfully");
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<Object> deleteEvent(Long id) {
        try
        {
            eventRepositoryObj.deleteById(id);
            return ResponseEntity.status(200).body("Event deleted successfully");
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> viewEvent() {
        try
        {
            return ResponseEntity.status(200).body(eventRepositoryObj.findAll());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> viewEventByName(String name) {
        try
        {
            return ResponseEntity.status(200).body(eventRepositoryObj.findByEventName(name));
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updateEvent(events eventsObj) {
        try
        {
            Optional<events> update = eventRepositoryObj.findById(eventsObj.getId());
            if(update.isPresent())
            {
                events updateObj = update.get();
                updateObj.setEventName(eventsObj.getEventName());
                updateObj.setEventDate(eventsObj.getEventDate());
                updateObj.setEventLocation(eventsObj.getEventLocation());
                updateObj.setRegistrationDetails(eventsObj.getRegistrationDetails());

                eventRepositoryObj.save(updateObj);
                return ResponseEntity.status(200).body("Event updated successfully");
            }
            else
            {
                return ResponseEntity.status(404).body("Not found");
            }

        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

package com.event.eventManagement.service;

import com.event.eventManagement.model.events;
import org.springframework.http.ResponseEntity;

public interface eventService {
    ResponseEntity<Object> addEvent(events eventsObj);
    ResponseEntity<Object> deleteEvent(Long id);
    ResponseEntity<Object> viewEvent();
    ResponseEntity<Object> viewEventByName(String name);
    ResponseEntity<Object> updateEvent(events eventsObj);


}

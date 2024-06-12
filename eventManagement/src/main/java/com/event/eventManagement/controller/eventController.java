package com.event.eventManagement.controller;

import com.event.eventManagement.model.events;
import com.event.eventManagement.service.eventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/test")
public class eventController {
    @Autowired
    private eventService eventServiceObj;

    @PostMapping("/addEvent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addEvent(@RequestBody events eventsObj)
    {
        return eventServiceObj.addEvent(eventsObj);
    }

    @GetMapping("/viewEvents")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> viewEvents()
    {
        return eventServiceObj.viewEvent();
    }

    @GetMapping("/viewEventsByName")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> viewEventsByName(@RequestParam String name)
    {
        return eventServiceObj.viewEventByName(name);
    }

    @PutMapping("/updateEvent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateEvent(@RequestBody events eventsObj)
    {
        return eventServiceObj.updateEvent(eventsObj);
    }

    @DeleteMapping("/deleteEvent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteEvent(@RequestParam Long id)
    {
        return eventServiceObj.deleteEvent(id);
    }
}

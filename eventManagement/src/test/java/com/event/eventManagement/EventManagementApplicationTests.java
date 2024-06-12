package com.event.eventManagement;

import com.event.eventManagement.model.events;
import com.event.eventManagement.repository.eventRepository;
import com.event.eventManagement.service.impl.eventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventManagementApplicationTests {
	@Mock
	private eventRepository eventRepositoryObj;

	@InjectMocks
	private eventServiceImpl eventService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddEventSuccess() {
		events event = new events();
		event.setEventName("Test Event");
		event.setEventDate(new Date());
		event.setEventLocation("Test Location");
		event.setRegistrationDetails("Test Details");

		when(eventRepositoryObj.save(any(events.class))).thenReturn(event);

		ResponseEntity<Object> response = eventService.addEvent(event);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Event added successfully", response.getBody());
	}

	@Test
	void testAddEventFailure() {
		events event = new events();
		when(eventRepositoryObj.save(any(events.class))).thenThrow(new RuntimeException("Database error"));

		ResponseEntity<Object> response = eventService.addEvent(event);

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Database error", response.getBody());
	}

	@Test
	void testDeleteEventSuccess() {
		doNothing().when(eventRepositoryObj).deleteById(1L);

		ResponseEntity<Object> response = eventService.deleteEvent(1L);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Event deleted successfully", response.getBody());
	}

	@Test
	void testDeleteEventFailure() {
		doThrow(new RuntimeException("Database error")).when(eventRepositoryObj).deleteById(1L);

		ResponseEntity<Object> response = eventService.deleteEvent(1L);

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Database error", response.getBody());
	}

	@Test
	void testViewEventSuccess() {
		List<events> eventsList = new ArrayList<>();
		events event = new events();
		event.setEventName("Test Event");
		eventsList.add(event);

		when(eventRepositoryObj.findAll()).thenReturn(eventsList);

		ResponseEntity<Object> response = eventService.viewEvent();

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(eventsList, response.getBody());
	}

	@Test
	void testViewEventFailure() {
		when(eventRepositoryObj.findAll()).thenThrow(new RuntimeException("Database error"));

		ResponseEntity<Object> response = eventService.viewEvent();

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Database error", response.getBody());
	}

	@Test
	void testViewEventByNameFailure() {
		when(eventRepositoryObj.findByEventName("Test Event")).thenThrow(new RuntimeException("Database error"));

		ResponseEntity<Object> response = eventService.viewEventByName("Test Event");

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Database error", response.getBody());
	}

	@Test
	void testUpdateEventSuccess() {
		events event = new events();
		event.setId(1L);
		event.setEventName("Updated Event");
		event.setEventDate(new Date());
		event.setEventLocation("Updated Location");
		event.setRegistrationDetails("Updated Details");

		when(eventRepositoryObj.findById(1L)).thenReturn(Optional.of(event));
		when(eventRepositoryObj.save(any(events.class))).thenReturn(event);

		ResponseEntity<Object> response = eventService.updateEvent(event);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Event updated successfully", response.getBody());
	}

	@Test
	void testUpdateEventNotFound() {
		events event = new events();
		event.setId(1L);

		when(eventRepositoryObj.findById(1L)).thenReturn(Optional.empty());

		ResponseEntity<Object> response = eventService.updateEvent(event);

		assertEquals(404, response.getStatusCodeValue());
		assertEquals("Not found", response.getBody());
	}

	@Test
	void testUpdateEventFailure() {
		events event = new events();
		event.setId(1L);

		when(eventRepositoryObj.findById(1L)).thenThrow(new RuntimeException("Database error"));

		ResponseEntity<Object> response = eventService.updateEvent(event);

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Database error", response.getBody());
	}
}

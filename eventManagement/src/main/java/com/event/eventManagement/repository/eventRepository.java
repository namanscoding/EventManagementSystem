package com.event.eventManagement.repository;

import com.event.eventManagement.model.events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface eventRepository extends JpaRepository<events, Long>{
    Optional<events> findByEventName(String Name);
}

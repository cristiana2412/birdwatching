package com.spirent.birdwatching.repository;

import com.spirent.birdwatching.entity.Sighting;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface SightingRepository extends ReactiveCrudRepository<Sighting, Long> {
    Flux<Sighting> findByBirdId(Long birdId);
    Flux<Sighting> findByLocationContainingIgnoreCase(String location);
    Flux<Sighting> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
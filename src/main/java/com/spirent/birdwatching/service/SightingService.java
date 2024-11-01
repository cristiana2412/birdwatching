package com.spirent.birdwatching.service;

import com.spirent.birdwatching.entity.Sighting;
import com.spirent.birdwatching.repository.SightingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SightingService {
    private SightingRepository sightingRepository;

    public Mono<Sighting> createSighting(Sighting sighting) {
        return sightingRepository.save(sighting);
    }

    public Flux<Sighting> getAllSightings() {
        return sightingRepository.findAll();
    }

    public Flux<Sighting> findSightingsByBirdId(Long birdId) {
        return sightingRepository.findByBirdId(birdId);
    }

    public Mono<Sighting> findSightingByLocationId(Long locationId) {
        return sightingRepository.findById(locationId);
    }

    public Flux<Sighting> findSightingsByDateTimeRange(LocalDateTime start, LocalDateTime end) {
        return sightingRepository.findByDateTimeBetween(start, end);
    }
}


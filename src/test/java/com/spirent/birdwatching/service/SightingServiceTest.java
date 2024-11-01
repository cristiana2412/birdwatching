package com.spirent.birdwatching.service;

import com.spirent.birdwatching.entity.Sighting;
import com.spirent.birdwatching.repository.SightingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SightingServiceTest {

    @Mock
    private SightingRepository sightingRepository;

    @InjectMocks
    private SightingService sightingService;

    private Sighting sampleSighting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleSighting = new Sighting();
        sampleSighting.setId(1L);
        sampleSighting.setBirdId(1L);
        sampleSighting.setLocation("Central Park");
        sampleSighting.setDateTime(LocalDateTime.now());
    }

    @Test
    void shouldCreateSighting() {
        when(sightingRepository.save(any(Sighting.class))).thenReturn(Mono.just(sampleSighting));

        Mono<Sighting> result = sightingService.createSighting(sampleSighting);

        StepVerifier.create(result)
                .expectNext(sampleSighting)
                .verifyComplete();
    }

    @Test
    void shouldReturnAllSightings() {
        when(sightingRepository.findAll()).thenReturn(Flux.just(sampleSighting));

        Flux<Sighting> result = sightingService.getAllSightings();

        StepVerifier.create(result)
                .expectNext(sampleSighting)
                .verifyComplete();
    }

    @Test
    void shouldFindSightingsByBirdId() {
        when(sightingRepository.findByBirdId(eq(1L))).thenReturn(Flux.just(sampleSighting));

        Flux<Sighting> result = sightingService.findSightingsByBirdId(1L);

        StepVerifier.create(result)
                .expectNext(sampleSighting)
                .verifyComplete();
    }

    @Test
    void shouldFindSightingsByLocation() {
        when(sightingRepository.findById(4353L)).thenReturn(Mono.just(sampleSighting));

        Mono<Sighting> result = sightingService.findSightingByLocationId(4353L);

        StepVerifier.create(result)
                .expectNext(sampleSighting)
                .verifyComplete();
    }

    @Test
    void shouldFindSightingsByDateTimeRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        when(sightingRepository.findByDateTimeBetween(start, end)).thenReturn(Flux.just(sampleSighting));

        Flux<Sighting> result = sightingService.findSightingsByDateTimeRange(start, end);

        StepVerifier.create(result)
                .expectNext(sampleSighting)
                .verifyComplete();
    }
}

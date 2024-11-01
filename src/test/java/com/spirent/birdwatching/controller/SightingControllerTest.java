package com.spirent.birdwatching.controller;

import com.spirent.birdwatching.entity.Sighting;
import com.spirent.birdwatching.error.GlobalExceptionHandler;
import com.spirent.birdwatching.service.SightingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = SightingController.class)
@Import(GlobalExceptionHandler.class) // Import exception handler
class SightingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SightingService sightingService;

    private Sighting sampleSighting;

    @BeforeEach
    void setUp() {
        sampleSighting = new Sighting();
        sampleSighting.setId(1L);
        sampleSighting.setBirdId(1L);
        sampleSighting.setLocation("Central Park");
        sampleSighting.setDateTime(LocalDateTime.now());
    }

    @Test
    void givenValidSighting_whenGetAllSightings_thenStatus200() {
        when(sightingService.getAllSightings()).thenReturn(Flux.just(sampleSighting));

        webTestClient.get().uri("/api/sightings")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Sighting.class)
                .hasSize(1)
                .contains(sampleSighting);
    }

    @Test
    void givenValidSighting_whenCreateSighting_thenStatus200() {
        when(sightingService.createSighting(any(Sighting.class))).thenReturn(Mono.just(sampleSighting));

        webTestClient.post().uri("/api/sightings")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleSighting)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(sampleSighting.getId())
                .jsonPath("$.birdId").isEqualTo(sampleSighting.getBirdId())
                .jsonPath("$.location").isEqualTo(sampleSighting.getLocation());
    }

    @Test
    void givenMissingSighting_whenGetSightingsByBirdId_thenStatus404() {
        when(sightingService.findSightingsByBirdId(999L)).thenReturn(Flux.empty());

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/api/sightings/search")
                                .queryParam("birdId", 999L)
                                .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Requested resource was not found.")
                .jsonPath("$.detailedErrorMessage").isEqualTo("Sighting not found for birdId: 999");
    }

    @Test
    void givenValidSighting_whenGetSightingsByBirdId_thenStatus200() {
        when(sightingService.findSightingsByBirdId(1L)).thenReturn(Flux.just(sampleSighting));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/api/sightings/search")
                                .queryParam("birdId", "1")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Sighting.class)
                .hasSize(1)
                .contains(sampleSighting);
    }

    @Test
    void givenMissingSighting_whenGetSightingByLocationId_thenStatus404() {
        when(sightingService.findSightingByLocationId(3256L)).thenReturn(Mono.empty());

        int invalidId = 3256;
        webTestClient.get().uri("/api/sightings/{id}", invalidId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Requested resource was not found.")
                .jsonPath("$.detailedErrorMessage").isEqualTo("Sighting not found with locationId: " + invalidId);

    }

    @Test
    void givenValidSighting_whenGetSightingByLocationId_thenStatus200() {
        when(sightingService.findSightingByLocationId(3256L)).thenReturn(Mono.just(sampleSighting));

        webTestClient.get().uri("/api/sightings/{id}", 3256)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Sighting.class)
                .hasSize(1)
                .contains(sampleSighting);
    }

    @Test
    void givenValidSighting_whenSearchSightingsByDateTimeRange_thenStatus200() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        when(sightingService.findSightingsByDateTimeRange(start, end)).thenReturn(Flux.just(sampleSighting));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/api/sightings/search")
                                .queryParam("startDateTime", start.toString())
                                .queryParam("endDateTime", end.toString())
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Sighting.class)
                .hasSize(1)
                .contains(sampleSighting);
    }

    @Test
    void givenValidSightingExist_whenSearchSightingsWithoutParameters_thenStatus200() {
        when(sightingService.getAllSightings()).thenReturn(Flux.just(sampleSighting));

        webTestClient.get().uri("/api/sightings/search")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Sighting.class)
                .hasSize(1)
                .contains(sampleSighting);
    }
}
package com.spirent.birdwatching.error;

import com.spirent.birdwatching.controller.BirdController;
import com.spirent.birdwatching.controller.SightingController;
import com.spirent.birdwatching.entity.Bird;
import com.spirent.birdwatching.error.exception.ConflictException;
import com.spirent.birdwatching.error.exception.ResourceNotFoundException;
import com.spirent.birdwatching.service.BirdService;
import com.spirent.birdwatching.service.SightingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {BirdController.class, SightingController.class})
@Import({GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private BirdService birdService;
    @MockBean
    private SightingService sightingService;

    @Test
    void givenResourceNotFound_whenGetBirdById_thenStatus404() {
        Long invalidId = 999L;
        when(birdService.getBirdById(invalidId)).thenReturn(Mono.error(new ResourceNotFoundException("Bird not found with id: " + invalidId)));

        webTestClient.get().uri("/api/birds/{id}", invalidId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Requested resource was not found.")
                .jsonPath("$.detailedErrorMessage").isEqualTo("Bird not found with id: " + invalidId);
    }

    @Test
    void givenBadRequest_whenPostBirdWithoutName_thenStatus400() {
        when(birdService.createBird(any(Bird.class)))
                .thenReturn(Mono.error(mock(WebExchangeBindException.class)));
        Bird invalidBird = new Bird();

        webTestClient.post().uri("/api/birds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBird)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("There are field validation errors.")
                .jsonPath("$.subErrors.name").isEqualTo("Bird name must not be empty.");
    }

    @Test
    void givenConflictException_whenPostDuplicateBird_thenStatus409() {
        Bird duplicateBird = new Bird();
        duplicateBird.setName("DuplicateBird");

        when(birdService.createBird(any(Bird.class)))
                .thenReturn(Mono.error(new ConflictException("Duplicate bird detected.")));

        webTestClient.post().uri("/api/birds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(duplicateBird)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.status").isEqualTo("CONFLICT")
                .jsonPath("$.message").isEqualTo("The resource was updated by another request. Please retry.")
                .jsonPath("$.detailedErrorMessage").isEqualTo("Duplicate bird detected.");
    }

    @Test
    void givenOptimisticLockingFailureException_whenUpdateBird_thenStatus409() {
        Long birdId = 1L;
        Bird updatedBird = new Bird();
        updatedBird.setId(birdId);
        updatedBird.setName("UpdatedBird");

        when(birdService.updateBird(birdId, updatedBird))
                .thenReturn(Mono.error(new OptimisticLockingFailureException("Optimistic locking failure.")));

        webTestClient.put().uri("/api/birds/{id}", birdId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBird)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.status").isEqualTo("CONFLICT")
                .jsonPath("$.message").isEqualTo("The resource was updated by another request. Please retry.")
                .jsonPath("$.detailedErrorMessage").isEqualTo("Optimistic locking failure.");
    }

    @Test
    void givenInvalidInput_whenGetBirdByInvalidId_thenStatus400() {
        webTestClient.get().uri("/api/birds/invalid-id")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("Invalid input.");
    }

    @Test
    void givenInvalidDateFormat_whenGetSightingByInvalidStartDateTime_thenStatus400AndCorrectMessage() {
        String invalidDate = "invalid-date-format";

        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/sightings/search")
                        .queryParam("startDateTime", invalidDate)
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("The string must represent a valid date-time and is parsed using ISO-8601 calendar system, such as 2007-12-03T10:15:30.");
    }

    @Test
    void givenGenericException_whenGetBirdByIdAndServerError_thenStatus500() {
        Long birdId = 1L;
        when(birdService.getBirdById(birdId)).thenThrow(new RuntimeException("Unexpected server error."));

        webTestClient.get().uri("/api/birds/{id}", birdId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.status").isEqualTo("INTERNAL_SERVER_ERROR")
                .jsonPath("$.message").isEqualTo("An unexpected error occurred.");
    }
}
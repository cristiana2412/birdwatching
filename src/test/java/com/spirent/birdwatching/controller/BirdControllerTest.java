package com.spirent.birdwatching.controller;

import com.spirent.birdwatching.entity.Bird;
import com.spirent.birdwatching.error.GlobalExceptionHandler;
import com.spirent.birdwatching.error.exception.ResourceNotFoundException;
import com.spirent.birdwatching.service.BirdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BirdController.class)
@Import(GlobalExceptionHandler.class)
class BirdControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BirdService birdService;

    @Test
    void testGetBirdById_Success() {
        Bird bird = new Bird();
        bird.setId(1L);
        bird.setName("Eagle");

        when(birdService.getBirdById(1L)).thenReturn(Mono.just(bird));

        webTestClient.get().uri("/api/birds/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Eagle");
    }

    @Test
    void testGetBirdById_NotFound() {
        when(birdService.getBirdById(1L)).thenReturn(Mono.error(new ResourceNotFoundException("Bird not found")));

        webTestClient.get().uri("/api/birds/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Requested resource was not found.");
    }

    @Test
    void testCreateBird_Success() {
        Bird bird = new Bird();
        bird.setName("Hawk");

        when(birdService.createBird(any(Bird.class))).thenReturn(Mono.just(bird));

        webTestClient.post().uri("/api/birds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bird)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Hawk");
    }

    @Test
    void testCreateBird_BadRequest() {
        Bird invalidBird = new Bird(); // Missing required fields

        webTestClient.post().uri("/api/birds")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBird)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("There are field validation errors.");
    }

    @Test
    void testUpdateBird_Success() {
        Long birdId = 1L;
        Bird updatedBird = new Bird();
        updatedBird.setId(birdId);
        updatedBird.setName("Updated Eagle");

        when(birdService.updateBird(birdId, updatedBird)).thenReturn(Mono.just(updatedBird));

        webTestClient.put().uri("/api/birds/{id}", birdId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBird)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Updated Eagle");
    }

    @Test
    void testUpdateBird_NotFound() {
        Long birdId = 1L;
        Bird updatedBird = new Bird();
        updatedBird.setId(birdId);
        updatedBird.setName("Updated Eagle");

        when(birdService.updateBird(birdId, updatedBird)).thenReturn(Mono.error(new ResourceNotFoundException("Bird not found")));

        webTestClient.put().uri("/api/birds/{id}", birdId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBird)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Requested resource was not found.");
    }

    @Test
    void testDeleteBird_Success() {
        when(birdService.deleteBird(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/birds/{id}", 1L)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testDeleteBird_NotFound() {
        when(birdService.deleteBird(1L)).thenReturn(Mono.error(new ResourceNotFoundException("Bird not found")));

        webTestClient.delete().uri("/api/birds/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Requested resource was not found.");
    }

    @Test
    void testGetAllBirds() {
        Bird bird1 = new Bird();
        bird1.setName("Eagle");

        Bird bird2 = new Bird();
        bird2.setName("Hawk");

        when(birdService.getAllBirds()).thenReturn(Flux.just(bird1, bird2));

        webTestClient.get().uri("/api/birds")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Eagle")
                .jsonPath("$[1].name").isEqualTo("Hawk");
    }

    @Test
    void testSearchBirdsWithoutParameters() {
        Bird bird = new Bird();
        bird.setName("Eagle");

        when(birdService.getAllBirds()).thenReturn(Flux.just(bird));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/api/birds/search").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Eagle");
    }

    @Test
    void testSearchBirdsByName() {
        Bird bird = new Bird();
        bird.setId(1L);
        bird.setName("Eagle");

        when(birdService.findBirdsByName("Eagle")).thenReturn(Flux.just(bird));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/api/birds/search").queryParam("name", "Eagle").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Eagle");
    }

    @Test
    void testSearchBirdsByColor() {
        Bird bird = new Bird();
        bird.setId(1L);
        bird.setColor("Brown");

        when(birdService.findBirdsByColor("Brown")).thenReturn(Flux.just(bird));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/api/birds/search").queryParam("color", "Brown").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].color").isEqualTo("Brown");
    }
}
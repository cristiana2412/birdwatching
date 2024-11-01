package com.spirent.birdwatching.service;

import com.spirent.birdwatching.entity.Bird;
import com.spirent.birdwatching.repository.BirdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BirdServiceTest {

    @Mock
    private BirdRepository birdRepository;

    @InjectMocks
    private BirdService birdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBirdById_Success() {
        Bird bird = new Bird();
        bird.setId(1L);
        bird.setName("Sparrow");

        when(birdRepository.findById(1L)).thenReturn(Mono.just(bird));

        Mono<Bird> result = birdService.getBirdById(1L);

        StepVerifier.create(result)
                .expectNext(bird)
                .verifyComplete();
    }

    @Test
    void testGetBirdById_NotFound() {
        when(birdRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<Bird> result = birdService.getBirdById(1L);

        StepVerifier.create(result)
                .expectNext() //todo check if empty
                .verifyComplete();
    }

    @Test
    void testCreateBird() {
        Bird bird = new Bird();
        bird.setName("Parrot");

        when(birdRepository.save(any(Bird.class))).thenReturn(Mono.just(bird));

        Mono<Bird> result = birdService.createBird(bird);

        StepVerifier.create(result)
                .expectNext(bird)
                .verifyComplete();
    }

    @Test
    void testGetAllBirds() {
        Bird bird1 = new Bird();
        bird1.setName("Eagle");
        Bird bird2 = new Bird();
        bird2.setName("Hawk");

        when(birdRepository.findAll()).thenReturn(Flux.just(bird1, bird2));

        Flux<Bird> result = birdService.getAllBirds();

        StepVerifier.create(result)
                .expectNext(bird1, bird2)
                .verifyComplete();
    }
}
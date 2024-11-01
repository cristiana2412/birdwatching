package com.spirent.birdwatching.repository;

import com.spirent.birdwatching.entity.Bird;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BirdRepository extends ReactiveCrudRepository<Bird, Long> {
    Flux<Bird> findByNameContainingIgnoreCase(String name);
    Flux<Bird> findByColorContainingIgnoreCase(String color);
}
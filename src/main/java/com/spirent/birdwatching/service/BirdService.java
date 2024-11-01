package com.spirent.birdwatching.service;

import com.spirent.birdwatching.entity.Bird;
import com.spirent.birdwatching.repository.BirdRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BirdService {
    private BirdRepository birdRepository;

    public Mono<Bird> createBird(Bird bird) {
        return birdRepository.save(bird);
    }

    public Mono<Bird> getBirdById(Long id) {
        return birdRepository.findById(id);
    }

    public Flux<Bird> getAllBirds() {
        return birdRepository.findAll();
    }

    public Mono<Bird> updateBird(Long id, Bird birdDetails) {
        return birdRepository.findById(id)
                .flatMap(bird -> {
                    bird.setName(birdDetails.getName());
                    bird.setColor(birdDetails.getColor());
                    bird.setWeight(birdDetails.getWeight());
                    bird.setHeight(birdDetails.getHeight());
                    return birdRepository.save(bird);
                });
    }

    public Mono<Void> deleteBird(Long id) {
        return birdRepository.deleteById(id);
    }

    public Flux<Bird> findBirdsByName(String name) {
        return birdRepository.findByNameContainingIgnoreCase(name);
    }

    public Flux<Bird> findBirdsByColor(String color) {
        return birdRepository.findByColorContainingIgnoreCase(color);
    }
}


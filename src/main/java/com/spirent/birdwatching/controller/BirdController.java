package com.spirent.birdwatching.controller;

import com.spirent.birdwatching.entity.Bird;
import com.spirent.birdwatching.error.exception.ResourceNotFoundException;
import com.spirent.birdwatching.service.BirdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/birds")
@Tag(name = "Birds", description = "Operations related to Birds")
public class BirdController {
    private BirdService birdService;

    @Operation(summary = "Create a new Bird", description = "Add a new bird to the database")
    @ApiResponse(
            responseCode = "201",
            description = "Created")
    @PostMapping
    public Mono<Bird> createBird(@Valid @RequestBody Bird bird) {
        return birdService.createBird(bird);
    }

    @Operation(summary = "Get Bird by ID", description = "Retrieve a bird's details by its ID")
    @ApiResponse(
            responseCode = "202",
            description = "Accepted")
    @GetMapping("/{id}")
    public Mono<Bird> getBirdById(@PathVariable Long id) {
        return birdService.getBirdById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bird not found with id: " + id)));
    }

    @GetMapping
    public Flux<Bird> getAllBirds() {
        return birdService.getAllBirds();
    }

    @PutMapping("/{id}")
    public Mono<Bird> updateBird(@PathVariable Long id, @Valid @RequestBody Bird birdDetails) {
        return birdService.updateBird(id, birdDetails);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBird(@PathVariable Long id) {
        return birdService.deleteBird(id);
    }

    @GetMapping("/search")
    public Flux<Bird> searchBirds(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color) {
        if (name != null) {
            return birdService.findBirdsByName(name);
        } else if (color != null) {
            return birdService.findBirdsByColor(color);
        } else {
            return birdService.getAllBirds();
        }
    }
}

package com.spirent.birdwatching.controller;

import com.spirent.birdwatching.entity.Bird;
import com.spirent.birdwatching.error.exception.ResourceNotFoundException;
import com.spirent.birdwatching.error.model.ApiError;
import com.spirent.birdwatching.service.BirdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @ApiResponse(
            responseCode = "400",
            description = "Bad request"
    )
    @PostMapping
    public Mono<Bird> createBird(@Valid @RequestBody Bird bird) {
        return birdService.createBird(bird);
    }

    @Operation(summary = "Get Bird by ID", description = "Retrieve a bird's details by its ID")
    @ApiResponse(
            responseCode = "200",
            description = "OK")
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                    schema = @Schema(implementation = ApiError.class)

            )
    )
    @GetMapping("/{id}")
    public Mono<Bird> getBirdById(@PathVariable Long id) {
        return birdService.getBirdById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bird not found with id: " + id)));
    }

    @Operation(summary = "Get all birds", description = "Retrieve a list with all birds in the database")
    @ApiResponse(
            responseCode = "200",
            description = "OK")
    @GetMapping
    public Flux<Bird> getAllBirds() {
        return birdService.getAllBirds();
    }

    @Operation(summary = "Update bird by id", description = "Update a bird's details by its ID")
    @ApiResponse(
            responseCode = "200",
            description = "OK")
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                    schema = @Schema(implementation = ApiError.class)

            )
    )
    @PutMapping("/{id}")
    public Mono<Bird> updateBird(@PathVariable Long id, @Valid @RequestBody Bird birdDetails) {
        return birdService.updateBird(id, birdDetails)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bird not found with ID: " + id)));
    }

    @Operation(summary = "Delete bird by id", description = "Delete a bird from the database by its ID")
    @ApiResponse(
            responseCode = "200",
            description = "OK")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteBird(@PathVariable Long id) {
        return birdService.deleteBird(id);
    }

    @Operation(summary = "Search birds", description = "Search birds by name or color")
    @ApiResponse(
            responseCode = "200",
            description = "OK")
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                    schema = @Schema(implementation = ApiError.class)

            )
    )
    @GetMapping("/search")
    public Flux<Bird> searchBirds(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color) {
        if (name != null) {
            return birdService.findBirdsByName(name)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bird not found with name: " + name)));
        } else if (color != null) {
            return birdService.findBirdsByColor(color)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bird not found with color: " + color)));
        } else {
            return birdService.getAllBirds();
        }
    }
}

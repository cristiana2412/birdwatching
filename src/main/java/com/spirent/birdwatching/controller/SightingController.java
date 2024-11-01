package com.spirent.birdwatching.controller;

import com.spirent.birdwatching.entity.Sighting;
import com.spirent.birdwatching.error.exception.ResourceNotFoundException;
import com.spirent.birdwatching.error.model.ApiError;
import com.spirent.birdwatching.service.SightingService;
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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sightings")
@Tag(name = "Birds", description = "Operations related to Birds's sightings")
public class SightingController {
    private SightingService sightingService;

    @Operation(summary = "Create a new sighting", description = "Add a new sighting to the database")
    @ApiResponse(
            responseCode = "201",
            description = "Created")
    @ApiResponse(
            responseCode = "400",
            description = "Bad request"
    )
    @PostMapping
    public Mono<Sighting> createSighting(@Valid @RequestBody Sighting sighting) {
        return sightingService.createSighting(sighting);
    }

    @Operation(summary = "Get sighting by location ID", description = "Retrieve a bird's sighting by location ID")
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
    public Mono<Sighting> getSightingsByLocationId(@PathVariable Long id) {
        return sightingService.findSightingByLocationId(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sighting not found with locationId: " + id)));
    }

    @Operation(summary = "Get all available sightings", description = "Retrieve a list of all available sightings")
    @ApiResponse(
            responseCode = "200",
            description = "OK")
    @GetMapping
    public Flux<Sighting> getAllSightings() {
        return sightingService.getAllSightings();
    }

    @Operation(summary = "Search sightings", description = "Search for sightings by bird ID or date-time range")
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
    public Flux<Sighting> searchSightings(
            @RequestParam(required = false) @NotNull Long birdId,
            @RequestParam(required = false) @NotNull String startDateTime,
            @RequestParam(required = false) @NotNull String endDateTime) {
        if (birdId != null) {
            return sightingService.findSightingsByBirdId(birdId)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sighting not found for birdId: " + birdId)));
        } else if (startDateTime != null || endDateTime != null) {
            LocalDateTime start = startDateTime != null ? LocalDateTime.parse(startDateTime) : LocalDateTime.now();
            LocalDateTime end = endDateTime != null ? LocalDateTime.parse(endDateTime) : LocalDateTime.now();
            return sightingService.findSightingsByDateTimeRange(start, end)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sighting not found between dates: " + start + " and " + end)));
        } else {
            return sightingService.getAllSightings();
        }
    }
}
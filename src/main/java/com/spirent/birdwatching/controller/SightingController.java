package com.spirent.birdwatching.controller;

import com.spirent.birdwatching.entity.Sighting;
import com.spirent.birdwatching.service.SightingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sightings")
@Tag(name = "Birds", description = "Operations related to Birds's sightings")
public class SightingController {
    private SightingService sightingService;

    @PostMapping
    public Mono<Sighting> createSighting(@Valid @RequestBody Sighting sighting) {
        return sightingService.createSighting(sighting);
    }

    @GetMapping
    public Flux<Sighting> getAllSightings() {
        return sightingService.getAllSightings();
    }

    @GetMapping("/search")
    public Flux<Sighting> searchSightings(
            @RequestParam(required = false) Long birdId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime) {
        if (birdId != null) {
            return sightingService.findSightingsByBirdId(birdId);
        } else if (location != null) {
            return sightingService.findSightingsByLocation(location);
        } else if (startDateTime != null || endDateTime != null) {
            LocalDateTime start = startDateTime != null ? LocalDateTime.parse(startDateTime) : LocalDateTime.now();
            LocalDateTime end = endDateTime != null ? LocalDateTime.parse(endDateTime) : LocalDateTime.now();
            return sightingService.findSightingsByDateTimeRange(start, end);
        } else {
            return sightingService.getAllSightings();
        }
    }
}

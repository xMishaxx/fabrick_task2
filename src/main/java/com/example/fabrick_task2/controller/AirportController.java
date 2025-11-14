package com.example.fabrick_task2.controller;

import com.example.fabrick_task2.model.Station;
import com.example.fabrick_task2.service.airport.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/fabrick/v1.0/airports")
@RequiredArgsConstructor
@Tag(name = "Airports", description = "Airport and observation stations API")
public class AirportController {

    private final AirportService airportService;

    @Operation(
            summary = "Find closest observation stations",
            description = "Given an airport ID, finds all the closest observation stations within a bounding box"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of stations",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Station.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Airport not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Error communicating with Aviation Weather API",
                    content = @Content
            )
    })
    @GetMapping("/{airportId}/stations")
    public ResponseEntity<List<Station>> getClosestStations(
            @Parameter(description = "ICAO airport code (e.g., KDEN, KARR)", required = true, example = "KDEN")
            @PathVariable String airportId,
            @Parameter(description = "Bounding box modifier in degrees (default 0.0)", example = "1.0")
            @RequestParam(required = false, defaultValue = "0.0") double closestBy) {

        log.info("GET /api/fabrick/v1.0/airports/{}/stations?closestBy={}", airportId, closestBy);

        List<Station> stations = airportService.findClosestStations(airportId, closestBy);

        return ResponseEntity.ok(stations);
    }
}

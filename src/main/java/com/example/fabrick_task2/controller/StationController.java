package com.example.fabrick_task2.controller;

import com.example.fabrick_task2.model.Airport;
import com.example.fabrick_task2.model.error.ErrorResponse;
import com.example.fabrick_task2.service.station.StationService;
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
@RequestMapping("/api/fabrick/v1.0/stations")
@RequiredArgsConstructor
@Tag(name = "Stations", description = "Observation stations and airports API")
public class StationController {

    private final StationService stationService;

    @Operation(
            summary = "Find closest airports",
            description = "Given a station ID, finds all the closest airports within a bounding box"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of airports",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Airport.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Station not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Error communicating with Aviation Weather API",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service unavailable - unable to access external API",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{stationId}/airports")
    public ResponseEntity<List<Airport>> getClosestAirports(
            @Parameter(description = "Station ID (ICAO code, e.g., KAFF, KCOS)", required = true, example = "KAFF")
            @PathVariable String stationId,
            @Parameter(description = "Bounding box modifier in degrees (default 0.0)", example = "1.0")
            @RequestParam(required = false, defaultValue = "0.0") double closestBy) {

        log.info("GET /api/fabrick/v1.0/stations/{}/airports?closestBy={}", stationId, closestBy);

        List<Airport> airports = stationService.findClosestAirports(stationId, closestBy);

        return ResponseEntity.ok(airports);
    }
}
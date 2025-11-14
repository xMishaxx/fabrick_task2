package com.example.fabrick_task2.controller;

import com.example.fabrick_task2.model.Station;
import com.example.fabrick_task2.service.airport.AirportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/fabrick/v1.0/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping("/{airportId}/stations")
    public ResponseEntity<List<Station>> getClosestStations(
            @PathVariable String airportId,
            @RequestParam(required = false, defaultValue = "0.0") double closestBy) {

        log.info("GET /api/fabrick/v1.0/airports/{}/stations?closestBy={}", airportId, closestBy);

        List<Station> stations = airportService.findClosestStations(airportId, closestBy);

        return ResponseEntity.ok(stations);
    }
}

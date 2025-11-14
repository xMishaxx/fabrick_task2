package com.example.fabrick_task2.controller;

import com.example.fabrick_task2.model.Airport;
import com.example.fabrick_task2.service.station.StationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/fabrick/v1.0/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping("/{stationId}/airports")
    public ResponseEntity<List<Airport>> getClosestAirports(
            @PathVariable String stationId,
            @RequestParam(required = false, defaultValue = "0.0") double closestBy) {

        log.info("GET /api/fabrick/v1.0/stations/{}/airports?closestBy={}", stationId, closestBy);

        List<Airport> airports = stationService.findClosestAirports(stationId, closestBy);

        return ResponseEntity.ok(airports);
    }
}
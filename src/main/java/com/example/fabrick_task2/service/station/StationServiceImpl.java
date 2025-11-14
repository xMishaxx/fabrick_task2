package com.example.fabrick_task2.service.station;

import com.example.fabrick_task2.client.AviationWeatherClient;
import com.example.fabrick_task2.exception.ResourceNotFoundException;
import com.example.fabrick_task2.model.Airport;
import com.example.fabrick_task2.model.external.AirportInfoResponse;
import com.example.fabrick_task2.model.external.StationInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final AviationWeatherClient aviationWeatherClient;

    @Override
    public List<Airport> findClosestAirports(String stationId, Double closestBy) {
        log.info("Finding closest airports for station: {} with closestBy: {}", stationId, closestBy);

        StationInfoResponse stationInfo = aviationWeatherClient.getStationInfo(stationId);
        if (stationInfo == null) {
            log.warn("Station not found: {}", stationId);
            throw new ResourceNotFoundException("Station not found with ID: " + stationId);
        }

        log.debug("Station found: {} at lat={}, lon={}", stationId, stationInfo.getLat(), stationInfo.getLon());

        double minLat = stationInfo.getLat() - closestBy;
        double maxLat = stationInfo.getLat() + closestBy;
        double minLon = stationInfo.getLon() - closestBy;
        double maxLon = stationInfo.getLon() + closestBy;

        log.debug("Bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}", minLat, minLon, maxLat, maxLon);

        List<AirportInfoResponse> airportsInBox = aviationWeatherClient.getAirportsInBoundingBox(
                minLat, minLon, maxLat, maxLon
        );

        if (airportsInBox == null || airportsInBox.isEmpty()) {
            log.info("No airports found in bounding box for station: {}", stationId);
            return Collections.emptyList();
        }

        log.info("Found {} airports for station: {}", airportsInBox.size(), stationId);

        return airportsInBox.stream()
                .map(this::mapToAirport)
                .collect(Collectors.toList());
    }

    private Airport mapToAirport(AirportInfoResponse response) {
        return Airport.builder()
                .id(response.getIcaoId())
                .name(response.getName())
                .state(response.getState())
                .country(response.getCountry())
                .latitude(response.getLat())
                .longitude(response.getLon())
                .elevation(response.getElev())
                .build();
    }
}
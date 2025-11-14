package com.example.fabrick_task2.service.airport;

import com.example.fabrick_task2.client.AviationWeatherClient;
import com.example.fabrick_task2.model.Station;
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
public class AirportServiceImpl implements AirportService {

    private final AviationWeatherClient aviationWeatherClient;

    @Override
    public List<Station> findClosestStations(String airportId, double closestBy) {
        log.info("Finding closest stations for airport: {} with closestBy: {}", airportId, closestBy);

        AirportInfoResponse airportInfo = aviationWeatherClient.getAirportInfo(airportId);
        if (airportInfo == null) {
            log.warn("Airport not found: {}", airportId);
            return Collections.emptyList();
        }

        log.debug("Airport found: {} at lat={}, lon={}", airportId, airportInfo.getLat(), airportInfo.getLon());

        double minLat = airportInfo.getLat() - closestBy;
        double maxLat = airportInfo.getLat() + closestBy;
        double minLon = airportInfo.getLon() - closestBy;
        double maxLon = airportInfo.getLon() + closestBy;

        log.debug("Bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}", minLat, minLon, maxLat, maxLon);

        List<StationInfoResponse> stationsInBox = aviationWeatherClient.getStationsInBoundingBox(
                minLat, minLon, maxLat, maxLon
        );

        if (stationsInBox == null || stationsInBox.isEmpty()) {
            log.info("No stations found in bounding box for airport: {}", airportId);
            return Collections.emptyList();
        }

        log.info("Found {} stations for airport: {}", stationsInBox.size(), airportId);

        return stationsInBox.stream()
                .map(this::mapToStation)
                .collect(Collectors.toList());
    }

    private Station mapToStation(StationInfoResponse response) {
        return Station.builder()
                .id(response.getId())
                .site(response.getSite())
                .state(response.getState())
                .country(response.getCountry())
                .latitude(response.getLat())
                .longitude(response.getLon())
                .elevation(response.getElev())
                .build();
    }
}
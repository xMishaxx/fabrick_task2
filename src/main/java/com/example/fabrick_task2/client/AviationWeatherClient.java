package com.example.fabrick_task2.client;

import com.example.fabrick_task2.exception.ResourceNotFoundException;
import com.example.fabrick_task2.model.external.AirportInfoResponse;
import com.example.fabrick_task2.model.external.StationInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class AviationWeatherClient {

    private final RestClient restClient;

    @Cacheable(value = "airportInfo", key = "#icaoCode")
    public AirportInfoResponse getAirportInfo(String icaoCode) {
        log.debug("Calling Aviation Weather API for airport: {}", icaoCode);

        List<AirportInfoResponse> airports = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airport")
                        .queryParam("ids", icaoCode)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (airports == null || airports.isEmpty()) {
            log.info("No airport found for ICAO code: {}", icaoCode);
            throw new ResourceNotFoundException("No airport found for ICAO code: " + icaoCode);
        }
        return airports.getFirst();
    }

    @Cacheable(value = "stationInfo", key = "#stationId")
    public StationInfoResponse getStationInfo(String stationId) {
        log.debug("Calling Aviation Weather API for station: {}", stationId);

        List<StationInfoResponse> stations = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stationinfo")
                        .queryParam("ids", stationId)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (stations == null || stations.isEmpty()) {
            log.info("No station found for ID: {}", stationId);
            throw new ResourceNotFoundException("No station found for ID: " + stationId);
        }

        return stations.getFirst();
    }

    @Cacheable(value = "stationsBbox", key = "#minLat + '_' + #minLon + '_' + #maxLat + '_' + #maxLon")
    public List<StationInfoResponse> getStationsInBoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
        String bbox = String.format("%f,%f,%f,%f", minLat, minLon, maxLat, maxLon);

        log.debug("Calling Aviation Weather API for stations in bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}",
                minLat, minLon, maxLat, maxLon);

        List<StationInfoResponse> stations = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stationinfo")
                        .queryParam("bbox", bbox)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        log.debug("Response body: {}", stations);

        if (stations == null) {
            log.warn("Empty response body for stations bounding box query");
            return Collections.emptyList();
        }

        return stations;
    }

    @Cacheable(value = "airportsBbox", key = "#minLat + '_' + #minLon + '_' + #maxLat + '_' + #maxLon")
    public List<AirportInfoResponse> getAirportsInBoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
        String bbox = String.format("%f,%f,%f,%f", minLat, minLon, maxLat, maxLon);

        log.debug("Calling Aviation Weather API for airports in bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}",
                minLat, minLon, maxLat, maxLon);

        List<AirportInfoResponse> airports = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airport")
                        .queryParam("bbox", bbox)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        log.debug("Response body: {}", airports);

        if (airports == null) {
            log.warn("Empty response body for airports bounding box query");
            return Collections.emptyList();
        }

        return airports;
    }
}
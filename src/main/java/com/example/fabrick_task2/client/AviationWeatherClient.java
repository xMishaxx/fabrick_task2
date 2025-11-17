package com.example.fabrick_task2.client;

import com.example.fabrick_task2.config.AviationWeatherApiProperties;
import com.example.fabrick_task2.model.external.AirportInfoResponse;
import com.example.fabrick_task2.model.external.StationInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class AviationWeatherClient {

    private final RestTemplate restTemplate;
    private final AviationWeatherApiProperties apiProperties;

    @Cacheable(value = "airportInfo", key = "#icaoCode")
    public AirportInfoResponse getAirportInfo(String icaoCode) {
        String url = UriComponentsBuilder.fromHttpUrl(apiProperties.getBaseUrl())
                .path("/airport")
                .queryParam("ids", icaoCode)
                .queryParam("format", "json")
                .toUriString();

        log.debug("Calling Aviation Weather API for airport: {}", icaoCode);

        try {
            ResponseEntity<List<AirportInfoResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<AirportInfoResponse>>() {
                    }
            );

            List<AirportInfoResponse> airports = response.getBody();
            if (airports == null || airports.isEmpty()) {
                log.warn("No airport found for ICAO code: {}", icaoCode);
                return null;
            }

            return airports.getFirst();
        } catch (Exception e) {
            log.error("Error calling Aviation Weather API for airport {}: {}", icaoCode, e.getMessage());
            return null;
        }
    }

    @Cacheable(value = "stationInfo", key = "#stationId")
    public StationInfoResponse getStationInfo(String stationId) {
        String url = UriComponentsBuilder.fromHttpUrl(apiProperties.getBaseUrl())
                .path("/stationinfo")
                .queryParam("ids", stationId)
                .queryParam("format", "json")
                .toUriString();

        log.debug("Calling Aviation Weather API for station: {}", stationId);

        try {
            ResponseEntity<List<StationInfoResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<StationInfoResponse>>() {
                    }
            );

            List<StationInfoResponse> stations = response.getBody();
            if (stations == null || stations.isEmpty()) {
                log.warn("No station found for ID: {}", stationId);
                return null;
            }

            return stations.getFirst();
        } catch (Exception e) {
            log.error("Error calling Aviation Weather API for station {}: {}", stationId, e.getMessage());
            return null;
        }
    }

    @Cacheable(value = "stationsBbox", key = "#minLat + '_' + #minLon + '_' + #maxLat + '_' + #maxLon")
    public List<StationInfoResponse> getStationsInBoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
        String bbox = String.format("%f,%f,%f,%f", minLat, minLon, maxLat, maxLon);

        String url = UriComponentsBuilder.fromHttpUrl(apiProperties.getBaseUrl())
                .path("/stationinfo")
                .queryParam("bbox", bbox)
                .queryParam("format", "json")
                .toUriString();

        log.debug("Calling Aviation Weather API for stations in bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}",
                minLat, minLon, maxLat, maxLon);
        log.debug("Full URL: {}", url);

        ResponseEntity<List<StationInfoResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StationInfoResponse>>() {
                }
        );

        log.debug("Response status: {}, body: {}", response.getStatusCode(), response.getBody());

        if (response.getBody() == null) {
            log.warn("Empty response body for stations bounding box query");
            return Collections.emptyList();
        }

        return response.getBody();
    }

    @Cacheable(value = "airportsBbox", key = "#minLat + '_' + #minLon + '_' + #maxLat + '_' + #maxLon")
    public List<AirportInfoResponse> getAirportsInBoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
        String bbox = String.format("%f,%f,%f,%f", minLat, minLon, maxLat, maxLon);

        String url = UriComponentsBuilder.fromHttpUrl(apiProperties.getBaseUrl())
                .path("/airport")
                .queryParam("bbox", bbox)
                .queryParam("format", "json")
                .toUriString();

        log.debug("Calling Aviation Weather API for airports in bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}",
                minLat, minLon, maxLat, maxLon);
        log.debug("Full URL: {}", url);

        ResponseEntity<List<AirportInfoResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AirportInfoResponse>>() {
                }
        );

        log.debug("Response status: {}, body: {}", response.getStatusCode(), response.getBody());

        if (response.getBody() == null) {
            log.warn("Empty response body for airports bounding box query");
            return Collections.emptyList();
        }

        return response.getBody();
    }
}
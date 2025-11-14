package com.example.fabrick_task2.service.station;

import com.example.fabrick_task2.client.AviationWeatherClient;
import com.example.fabrick_task2.model.Airport;
import com.example.fabrick_task2.model.external.AirportInfoResponse;
import com.example.fabrick_task2.model.external.StationInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationServiceImplTest {

    @Mock
    private AviationWeatherClient aviationWeatherClient;

    @InjectMocks
    private StationServiceImpl stationService;

    private StationInfoResponse stationInfo;
    private AirportInfoResponse airport1;
    private AirportInfoResponse airport2;

    @BeforeEach
    void setUp() {
        stationInfo = new StationInfoResponse();
        stationInfo.setId("KAFF");
        stationInfo.setSite("Air Force Academy Arfld");
        stationInfo.setState("CO");
        stationInfo.setCountry("US");
        stationInfo.setLat(38.971);
        stationInfo.setLon(-104.816);
        stationInfo.setElev(2003);

        airport1 = new AirportInfoResponse();
        airport1.setIcaoId("KDEN");
        airport1.setName("DENVER INTL");
        airport1.setState("CO");
        airport1.setCountry("US");
        airport1.setLat(39.8617);
        airport1.setLon(-104.6732);
        airport1.setElev(1656.6);

        airport2 = new AirportInfoResponse();
        airport2.setIcaoId("KCOS");
        airport2.setName("COLORADO SPRINGS");
        airport2.setState("CO");
        airport2.setCountry("US");
        airport2.setLat(38.805);
        airport2.setLon(-104.700);
        airport2.setElev(1881.0);
    }

    @Test
    void testFindClosestAirports_Success() {
        String stationId = "KAFF";
        double closestBy = 1.0;

        when(aviationWeatherClient.getStationInfo(stationId)).thenReturn(stationInfo);
        when(aviationWeatherClient.getAirportsInBoundingBox(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        )).thenReturn(Arrays.asList(airport1, airport2));

        List<Airport> result = stationService.findClosestAirports(stationId, closestBy);

        assertNotNull(result);
        assertEquals(2, result.size());

        Airport firstAirport = result.get(0);
        assertEquals("KDEN", firstAirport.getId());

        verify(aviationWeatherClient, times(1)).getStationInfo(stationId);
        verify(aviationWeatherClient, times(1)).getAirportsInBoundingBox(
                eq(37.971), eq(-105.816), eq(39.971), eq(-103.816)
        );
    }

    @Test
    void testFindClosestAirports_StationNotFound() {
        String stationId = "XXXX";
        double closestBy = 0.0;

        when(aviationWeatherClient.getStationInfo(stationId)).thenReturn(null);

        List<Airport> result = stationService.findClosestAirports(stationId, closestBy);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(aviationWeatherClient, times(1)).getStationInfo(stationId);
        verify(aviationWeatherClient, never()).getAirportsInBoundingBox(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        );
    }

    @Test
    void testFindClosestAirports_NoAirportsInBoundingBox() {
        String stationId = "KAFF";
        double closestBy = 0.1;

        when(aviationWeatherClient.getStationInfo(stationId)).thenReturn(stationInfo);
        when(aviationWeatherClient.getAirportsInBoundingBox(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        )).thenReturn(Collections.emptyList());

        List<Airport> result = stationService.findClosestAirports(stationId, closestBy);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(aviationWeatherClient, times(1)).getStationInfo(stationId);
        verify(aviationWeatherClient, times(1)).getAirportsInBoundingBox(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        );
    }
}
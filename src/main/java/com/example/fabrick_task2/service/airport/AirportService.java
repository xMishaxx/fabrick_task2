package com.example.fabrick_task2.service.airport;

import com.example.fabrick_task2.model.Station;

import java.util.List;


public interface AirportService {

    List<Station> findClosestStations(String airportId, double closestBy);

}
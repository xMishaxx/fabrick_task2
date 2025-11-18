package com.example.fabrick_task2.service.station;

import com.example.fabrick_task2.model.Airport;

import java.util.List;


public interface StationService {

    List<Airport> findClosestAirports(String stationId, Double closestBy);
}
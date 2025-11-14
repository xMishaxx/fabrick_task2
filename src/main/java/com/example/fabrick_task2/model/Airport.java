package com.example.fabrick_task2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airport {
    private String id;
    private String name;
    private String state;
    private String country;
    private Double latitude;
    private Double longitude;
    private Double elevation;
}
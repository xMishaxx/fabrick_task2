package com.example.fabrick_task2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoundingBox {
    private double minLatitude;

    private double minLongitude;

    private double maxLatitude;

    private double maxLongitude;
}
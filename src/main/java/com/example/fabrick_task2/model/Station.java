package com.example.fabrick_task2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    private String id;
    private String site;
    private String state;
    private String country;
    private Double latitude;
    private Double longitude;
    private Integer elevation;
}
package com.example.fabrick_task2.model.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response model returned when an error occurs")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2025-01-15T10:30:45")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error type or reason phrase", example = "Not Found")
    private String error;

    @Schema(description = "Detailed error message", example = "Airport with ID KDEN not found")
    private String message;

    @Schema(description = "Request path that caused the error", example = "/api/fabrick/v1.0/airports/KDEN/stations")
    private String path;
}
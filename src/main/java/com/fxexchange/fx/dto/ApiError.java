package com.fxexchange.fx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;

    public ApiError(HttpStatus status, String message, String errorCode) {
        this(status, message, errorCode, LocalDateTime.now());
    }
}

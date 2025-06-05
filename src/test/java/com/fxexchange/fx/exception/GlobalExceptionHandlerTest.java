package com.fxexchange.fx.exception;

import com.fxexchange.fx.dto.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        ResponseEntity<ApiError> response = handler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ERR_NOT_FOUND", response.getBody().getErrorCode());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    void testHandleInvalidRequest() {
        InvalidRequestException ex = new InvalidRequestException("Invalid request");
        ResponseEntity<ApiError> response = handler.handleInvalidRequest(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ERR_INVALID_REQUEST", response.getBody().getErrorCode());
        assertEquals("Invalid request", response.getBody().getMessage());
    }

    @Test
    void testHandleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        ResponseEntity<ApiError> response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ERR_TYPE_MISMATCH", response.getBody().getErrorCode());
        assertEquals("Invalid parameter type", response.getBody().getMessage());
    }

    @Test
    void testHandleValidation() {
        ConstraintViolationException ex = new ConstraintViolationException("Field is required", null);
        ResponseEntity<ApiError> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ERR_VALIDATION", response.getBody().getErrorCode());
        assertTrue(response.getBody().getMessage().contains("Validation failed:"));
    }

    @Test
    void testHandleAllUncaughtExceptions() {
        Exception ex = new RuntimeException("Something went wrong");
        ResponseEntity<ApiError> response = handler.handleAllUncaughtExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERR_INTERNAL", response.getBody().getErrorCode());
        assertTrue(response.getBody().getMessage().contains("An unexpected error occurred"));
    }
}

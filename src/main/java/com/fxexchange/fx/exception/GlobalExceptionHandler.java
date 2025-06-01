package com.fxexchange.fx.exception;

import com.fxexchange.fx.dto.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), "ERR_NOT_FOUND");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRequest(InvalidRequestException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), "ERR_INVALID_REQUEST");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Invalid parameter type", "ERR_TYPE_MISMATCH");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidation(ConstraintViolationException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed: " + ex.getMessage(), "ERR_VALIDATION");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtExceptions(Exception ex) {
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", "ERR_INTERNAL");
        return new ResponseEntity<>(error, error.getStatus());
    }
}

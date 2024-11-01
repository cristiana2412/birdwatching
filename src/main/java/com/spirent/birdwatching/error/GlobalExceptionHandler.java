package com.spirent.birdwatching.error;

import com.spirent.birdwatching.error.exception.ConflictException;
import com.spirent.birdwatching.error.exception.ResourceNotFoundException;
import com.spirent.birdwatching.error.model.ApiError;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.time.format.DateTimeParseException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND, "Requested resource was not found.", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiError> handleValidationException(WebExchangeBindException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getFieldErrors().forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, "There are field validation errors.", fieldErrors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConflictException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ApiError> handleOptimisticLockingFailure(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.CONFLICT, "The resource was updated by another request. Please retry.", ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ApiError> handleInvalidInput(ServerWebInputException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, "Invalid input.", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiError> handleDateTimeParseException(DateTimeParseException ex) {
        String message = "The string must represent a valid date-time and is parsed using ISO-8601 calendar system, such as 2007-12-03T10:15:30.";
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, message, ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.put("message", "An unexpected error occurred.");
        response.put("errorCause", ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


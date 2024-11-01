package com.spirent.birdwatching.error.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String detailedErrorMessage;
    private Map<String, String> subErrors;

    private ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.detailedErrorMessage = ex.getMessage();
    }

    public ApiError(HttpStatus status, String message, Map<String, String> subErrors) {
        this();
        this.status = status;
        this.message = message;
        this.subErrors = subErrors;
    }
}

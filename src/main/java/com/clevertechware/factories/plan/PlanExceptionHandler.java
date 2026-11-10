package com.clevertechware.factories.plan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PlanExceptionHandler {

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<Void> planNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PlanUnavailableException.class)
    public ResponseEntity<Void> planUnavailable() {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}

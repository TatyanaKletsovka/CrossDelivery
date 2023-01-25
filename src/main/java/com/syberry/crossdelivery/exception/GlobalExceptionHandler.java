package com.syberry.crossdelivery.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends Exception {

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    private Map<String, List<Map<String, Object>>> getErrorsMap(Map<String, Object> errors) {
        Map<String, List<Map<String, Object>>> errorResponse = new HashMap<>();
        errorResponse.put("errors", List.of(errors));
        return errorResponse;
    }

    @ExceptionHandler({
            AccessException.class,
            EntityNotFoundException.class,
            InvalidArgumentTypeException.class,
            TokenRefreshException.class,
            UpdateException.class,
            ValidationException.class
    })
    public final ResponseEntity<Map<String, List<String>>> customExceptionHandler(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<Map<String, Object>>>> validationErrorsHandler(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            error.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        Map<String, Object> response = new HashMap<>();
        response.put("ValidationErrors", error);
        return new ResponseEntity<>(getErrorsMap(response), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}

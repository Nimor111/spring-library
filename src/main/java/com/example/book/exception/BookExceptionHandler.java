package com.example.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BookExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse bookNotFoundHandler(BookNotFoundException ex) {
        return new ErrorResponse(ex, HttpStatus.NOT_FOUND, "/api/v1/books");
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse authorNotFoundHandler(AuthorNotFoundException ex) {
        return new ErrorResponse(ex, HttpStatus.NOT_FOUND, "/api/v1/authors");
    }

    @ExceptionHandler(StoreClientException.class)
    ResponseEntity<ErrorResponse> storeClientExceptionHandler(StoreClientException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex, ex.getStatusCode(), "/api/v1/stores"), ex.getStatusCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

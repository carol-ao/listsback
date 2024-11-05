package com.randomstuff.lists.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
        StandardError err = new StandardError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found.",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
        ValidationError err = new ValidationError(
                Instant.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation exception.",
                e.getMessage(),
                request.getRequestURI(),
                new ArrayList<FieldMessage>()
        );

        for(FieldError f: e.getFieldErrors()){
            err.errors().add(new FieldMessage(f.getField(), f.getDefaultMessage()));
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<StandardError> validation(EmailAlreadyRegisteredException e, HttpServletRequest request){
        StandardError err = new StandardError(
                Instant.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "E-mail already exists exception.",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }
}

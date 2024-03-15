package com.auth.authmicroservice.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(value = IllegalAgeException.class)
    public ResponseEntity<?> illegalAgeExceptionHandle(IllegalAgeException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(
                e.getMessage(),e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }
    @ExceptionHandler(value = ResourceAccessException.class)
    public ResponseEntity<?> resourceAccessExceptionHandle(ResourceAccessException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException exception = new ApiException(
                e.getMessage(),e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }
    @ExceptionHandler(value = EmailDoesntExistException.class)
    public ResponseEntity<?> emailDoesntExistExceptionHandle(EmailDoesntExistException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(
                e.getMessage(), e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> fieldsValidateExceptionHandle(MethodArgumentNotValidException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException exception = new ApiException(
                e.getMessage(), e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }

}

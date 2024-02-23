package com.by.chaplygin.demo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = {PersonNotFoundException.class})
    public ResponseEntity<Object> handlePersonNotFoundException(PersonNotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(
                e.getMessage(),e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {EmptyPartyListException.class})
    public ResponseEntity<Object> handleEmptyPartyListException(EmptyPartyListException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(
                e.getMessage(),e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e){
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiException exception = new ApiException(
                e.getMessage(),e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> handleAuthException(AuthenticationException e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiException exception = new ApiException(
                e.getMessage(),e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }


}


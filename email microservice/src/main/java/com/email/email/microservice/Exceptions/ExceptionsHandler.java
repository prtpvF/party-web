package com.email.email.microservice.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = {MailException.class})
    public ResponseEntity<Object> handleMailException(MailException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException exception = new ApiException(
                e.getMessage(), e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {MailSendException.class})
    public ResponseEntity<Object> handleMailSendException(MailSendException e) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        ApiException exception = new ApiException(
                e.getMessage(), e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {MailParseException.class})
    public ResponseEntity<Object> handleMailParseException(MailParseException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiException exception = new ApiException(
                e.getMessage(), e, status, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }
}

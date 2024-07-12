package by.intexsoft.diplom.auth.exception.handler;

import by.intexsoft.diplom.auth.exception.ApiException;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(PersonAlreadyExists.class)
    public ResponseEntity<Object> PersonAlreadyExistsHandler(PersonAlreadyExists e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException exception = new ApiException(e.getMessage(), e, status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(exception, status);
    }
}

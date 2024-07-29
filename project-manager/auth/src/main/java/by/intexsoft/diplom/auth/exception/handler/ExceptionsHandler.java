package by.intexsoft.diplom.auth.exception.handler;

import by.intexsoft.diplom.auth.exception.ApiException;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.SignatureException;


@ControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler(value = {PersonAlreadyExists.class, TokenExpiredException.class})
    public ResponseEntity<Object> personAlreadyExistsHandler(RuntimeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException exception = new ApiException(ex, status);
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Object> personNotFoundExceptionHandler(PersonNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(e, status);
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = SignatureException.class)
    public ResponseEntity signatureVerificationExceptionHandler(SignatureVerificationException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        String message = "something wrong with authorization";
        log.warn("something happened with authorization");
        return new ResponseEntity(message, status);
    }
}
package by.intexsoft.diplom.publicapi.exception.handler;

import by.intexsoft.diplom.publicapi.exception.*;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionsHandler {


    @ExceptionHandler(value = {PartyNotFoundException.class, PersonNotFoundException.class, NoPartiesInCityException.class})
    public ResponseEntity<Object> partyNotFoundExceptionHandler(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(e, status);
        log.error(e.getMessage());
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(PasswordsDontMatchException.class)
    public ResponseEntity<Object> passwordsDontMatchExceptionHandler(PasswordsDontMatchException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiException exception = new ApiException(ex, status);
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {TokenExpiredException.class, JWTDecodeException.class})
    public ResponseEntity<Object> tokenExceptionHandler(RuntimeException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiException exception = new ApiException(ex, status);
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = UsernameIsTakenException.class)
    public ResponseEntity<Object> usernameIsTakenExceptionHandler(UsernameIsTakenException ex) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        ApiException exception = new ApiException(ex, status);
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<Object> updateExceptionHandler(UpdateException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiException exception = new ApiException(ex, status);
        return new ResponseEntity<>(exception, status);
    }
}
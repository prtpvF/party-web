package by.intexsoft.diplom.publicapi.exception.handler;

import by.intexsoft.diplom.publicapi.exception.*;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionsHandler {
    private final Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(value = {PartyNotFoundException.class, PersonNotFoundException.class, NoPartiesInCityException.class})
    public ResponseEntity<Object> partyNotFoundExceptionHandler(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(e, status);
        logger.error(e.getMessage());
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(PasswordsDontMatchException.class)
    public ResponseEntity<Object> passwordsDontMatchExceptionHandler(PasswordsDontMatchException ex){
        HttpStatus status = HttpStatus.CONFLICT;
        ApiException exception = new ApiException(ex, status);
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {TokenExpiredException.class, JWTDecodeException.class})
    public ResponseEntity<Object> TokenExpiredExceptionHandler(RuntimeException ex){
        HttpStatus status = HttpStatus.CONFLICT;
        ApiException exception = new ApiException(ex, status);
        return new ResponseEntity<>(exception, status);
    }



}

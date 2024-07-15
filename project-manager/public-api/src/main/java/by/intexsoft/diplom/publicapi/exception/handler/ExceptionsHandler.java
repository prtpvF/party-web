package by.intexsoft.diplom.publicapi.exception.handler;

import by.intexsoft.diplom.publicapi.exception.ApiException;
import by.intexsoft.diplom.publicapi.exception.NoPartiesInCityException;
import by.intexsoft.diplom.publicapi.exception.PartyNotFoundException;
import by.intexsoft.diplom.publicapi.exception.PersonNotFoundException;
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



}

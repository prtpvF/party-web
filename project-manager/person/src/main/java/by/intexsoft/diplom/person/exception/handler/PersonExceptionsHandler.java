package by.intexsoft.diplom.person.exception.handler;

import by.intexsoft.diplom.person.exception.AccountIsBannedException;
import by.intexsoft.diplom.person.exception.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.InvalidClassException;

@ControllerAdvice
@Slf4j
public class PersonExceptionsHandler {

    @ExceptionHandler(value = PersonNotFoundException.class)
    public ResponseEntity personNotFoundExceptionHandler(PersonNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.info("cannot find person by username");
        return new ResponseEntity(ex, status);
    }

    @ExceptionHandler(value = AccountIsBannedException.class)
    public ResponseEntity accountIsBannedExceptionHandler(AccountIsBannedException ex) {
        HttpStatus status = HttpStatus.LOCKED;
        return new ResponseEntity(ex, status);
    }

    @ExceptionHandler(value = InvalidClassException.class)
    public ResponseEntity invalidFileSizeExceptionHandler(InvalidClassException ex) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        return new ResponseEntity(ex, status);
    }

}

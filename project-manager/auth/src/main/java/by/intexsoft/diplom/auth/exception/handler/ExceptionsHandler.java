package by.intexsoft.diplom.auth.exception.handler;

import by.intexsoft.diplom.auth.exception.CodesAreNotEqualException;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class ExceptionsHandler {

        @ExceptionHandler(PersonNotFoundException.class)
        public ResponseEntity<Object> personNotFoundExceptionHandler(PersonNotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(e.getMessage(), status);
        }

        @ExceptionHandler(value = CodesAreNotEqualException.class)
        public ResponseEntity codesAreNotEqualExceptionHandler(CodesAreNotEqualException ex) {
            HttpStatus status = HttpStatus.CONFLICT;
            return new ResponseEntity(ex.getMessage(), status);
        }
}
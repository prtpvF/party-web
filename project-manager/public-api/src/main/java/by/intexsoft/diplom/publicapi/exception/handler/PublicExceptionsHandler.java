package by.intexsoft.diplom.publicapi.exception.handler;

import by.intexsoft.diplom.publicapi.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class PublicExceptionsHandler {

        @ExceptionHandler(value = {PartyNotFoundException.class,
                                    PersonNotFoundException.class,
                                    NoPartiesInCityException.class})
        public ResponseEntity<Object> partyNotFoundExceptionHandler(RuntimeException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), status);
        }

        @ExceptionHandler(PasswordsDontMatchException.class)
        public ResponseEntity<Object> passwordsDontMatchExceptionHandler(PasswordsDontMatchException ex) {
            HttpStatus status = HttpStatus.CONFLICT;
            return new ResponseEntity<>(ex.getMessage(), status);
        }

        @ExceptionHandler(value = UsernameIsTakenException.class)
        public ResponseEntity<Object> usernameIsTakenExceptionHandler(UsernameIsTakenException ex) {
            HttpStatus status = HttpStatus.BAD_GATEWAY;
            return new ResponseEntity<>(ex.getMessage(), status);
        }

        @ExceptionHandler(UpdateException.class)
        public ResponseEntity<Object> updateExceptionHandler(UpdateException ex) {
            HttpStatus status = HttpStatus.CONFLICT;
            return new ResponseEntity<>(ex.getMessage(), status);
        }
}
package by.intexsoft.diplom.publicapi.exception.handler;

import by.intexsoft.diplom.publicapi.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class PublicExceptionsHandler {

        @ExceptionHandler(value = {PartyNotFoundException.class,
                                    PersonNotFoundException.class,
                                    NoPartiesInCityException.class})
        public ResponseEntity<Object> partyNotFoundExceptionHandler(RuntimeException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(PasswordsDontMatchException.class)
        public ResponseEntity<Object> passwordsDontMatchExceptionHandler(PasswordsDontMatchException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), CONFLICT);
        }

        @ExceptionHandler(value = UsernameIsTakenException.class)
        public ResponseEntity<Object> usernameIsTakenExceptionHandler(UsernameIsTakenException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), BAD_GATEWAY);
        }

        @ExceptionHandler(UpdateException.class)
        public ResponseEntity<Object> updateExceptionHandler(UpdateException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), CONFLICT);
        }

        @ExceptionHandler(HttpClientErrorException.class)
        public ResponseEntity<Object> httpClientErrorExceptionHandler(HttpClientErrorException ex) {
             log.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), CONFLICT);
        }

        @ExceptionHandler(HttpServerErrorException.class)
        public ResponseEntity<Object> httpServerErrorExceptionHandler(HttpServerErrorException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), CONFLICT);
        }

        @ExceptionHandler(ResourceAccessException.class)
        public ResponseEntity<Object> resourceAccessExceptionHandler(ResourceAccessException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), BAD_GATEWAY);
        }

        @ExceptionHandler(UnavailablePageNumberException.class)
        public ResponseEntity<Object> unavailablePageNumberExceptionHandler(UnavailablePageNumberException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), BAD_GATEWAY);
        }
}
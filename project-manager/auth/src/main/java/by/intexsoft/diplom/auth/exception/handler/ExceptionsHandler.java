package by.intexsoft.diplom.auth.exception.handler;

import by.intexsoft.diplom.auth.exception.CodesAreNotEqualException;
import by.intexsoft.diplom.auth.exception.InvalidDataException;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.SocketTimeoutException;


@ControllerAdvice
@Slf4j
public class ExceptionsHandler {

        @ExceptionHandler(PersonNotFoundException.class)
        public ResponseEntity<Object> personNotFoundExceptionHandler(PersonNotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(e.getMessage(), status);
        }

        @ExceptionHandler(PersonAlreadyExists.class)
        public ResponseEntity<Object> personAlreadyExistsHandler(PersonAlreadyExists e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(e.getMessage(), status);
        }

        @ExceptionHandler(InternalServerErrorException.class)
        public ResponseEntity<String> handleInternalServerErrorException(InternalServerErrorException ex) {
            String errorMessage = "An internal server error occurred.";
            if (ex.getResponse() != null && ex.getResponse().hasEntity()) {
                errorMessage = ex.getResponse().readEntity(String.class);
            }
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleException(Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(value = CodesAreNotEqualException.class)
        public ResponseEntity<String> codesAreNotEqualExceptionHandler(CodesAreNotEqualException ex) {
            HttpStatus status = HttpStatus.CONFLICT;
            return new ResponseEntity<>(ex.getMessage(), status);
        }

        @ExceptionHandler(value = SocketTimeoutException.class)
        public ResponseEntity<String> socketTimeoutExceptionHandler(SocketTimeoutException ex) {
            HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
            return new ResponseEntity<>(ex.getMessage(), status);
        }

        @ExceptionHandler(value = InvalidDataException.class)
        public ResponseEntity<String> invalidDataExceptionHandler(InvalidDataException ex) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(ex.getMessage(), status);
        }
}
package by.intexsoft.diplom.admin.exception.handler;

import by.intexsoft.diplom.admin.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class AdminExceptionHandler {

        @ExceptionHandler(value = EmptyPartiesListException.class)
        public ResponseEntity<String> emptyPartiesListExceptionHandler(EmptyPartiesListException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(PartyNotFoundException.class)
        public ResponseEntity<String> partyNotFoundExceptionHandler(PartyNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(IllegalPartyStatusException.class)
        public ResponseEntity<String> illegalPartyStatusExceptionHandler(IllegalPartyStatusException e) {
            return new ResponseEntity<> (e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(StatusNotFoundException.class)
        public ResponseEntity<String> statusNotFoundExceptionHandler(StatusNotFoundException e) {
            return new ResponseEntity<> (e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(PersonNotFoundException.class)
        public ResponseEntity<String> personNotFoundExceptionHandler(PersonNotFoundException e) {
            return new ResponseEntity<> (e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(IllegalPartyDataException.class)
        public ResponseEntity<String> illegalPartyDataExceptionHandler(IllegalPartyDataException e) {
            return new ResponseEntity<> (e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(IllegalPartyOwnerException.class)
        public ResponseEntity<String> IllegalPartyOwnerExceptionHandler(IllegalPartyOwnerException e) {
            return new ResponseEntity<> (e.getMessage(), BAD_REQUEST);
        }
}

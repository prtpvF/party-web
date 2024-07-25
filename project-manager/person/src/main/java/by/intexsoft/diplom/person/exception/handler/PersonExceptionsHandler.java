package by.intexsoft.diplom.person.exception.handler;

import by.intexsoft.diplom.person.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.InvalidClassException;

@ControllerAdvice
@Slf4j
public class PersonExceptionsHandler {

        @ExceptionHandler(value = PersonNotFoundException.class)
        public ResponseEntity personNotFoundExceptionHandler(PersonNotFoundException ex) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            log.info("cannot find person by username");
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = AccountIsBannedException.class)
        public ResponseEntity accountIsBannedExceptionHandler(AccountIsBannedException ex) {
            HttpStatus status = HttpStatus.LOCKED;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = InvalidClassException.class)
        public ResponseEntity invalidFileSizeExceptionHandler(InvalidClassException ex) {
            HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = MaxUploadSizeExceededException.class)
        public ResponseEntity invalidFileSizeExceptionHandler(MaxUploadSizeExceededException ex) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = InvalidFileTypeException.class)
        public ResponseEntity invalidFileTypeExceptionHandler(InvalidFileTypeException ex) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = PartyNotFoundException.class)
        public ResponseEntity partyNotFoundExceptionHandler(PartyNotFoundException ex) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = IllegalPartyOrganizerException.class)
        public ResponseEntity illegalPartyOrganizerHandler(IllegalPartyOrganizerException ex) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = RequestAlreadyExistException.class)
        public ResponseEntity requestAlreadyExistExceptionHandler(RequestAlreadyExistException ex) {
            HttpStatus status = HttpStatus.FOUND;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = StatusNotFoundException.class)
        public ResponseEntity statusNotFoundExceptionHandler(StatusNotFoundException ex) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = RequestNotFoundException.class)
        public ResponseEntity requestNotFoundExceptionHandler(RequestNotFoundException ex) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = ParticipationRequestAlsoExists.class)
        public ResponseEntity participationRequestAlsoExistsHandler(ParticipationRequestAlsoExists ex) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = ParticipationRequestNotFoundException.class)
        public ResponseEntity participationRequestNotFoundExceptionHandler(ParticipationRequestNotFoundException ex) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return new ResponseEntity(ex.getMessage(), status);
        }

        @ExceptionHandler(value = InvalidRequestOwner.class)
        public ResponseEntity invalidRequestOwnerHandler(InvalidRequestOwner ex) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity(ex.getMessage(), status);
        }
}
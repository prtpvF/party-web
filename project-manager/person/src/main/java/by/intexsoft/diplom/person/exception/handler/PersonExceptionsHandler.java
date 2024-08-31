package by.intexsoft.diplom.person.exception.handler;

import by.intexsoft.diplom.person.exception.*;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.io.InvalidClassException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class PersonExceptionsHandler {

        @ExceptionHandler(value = PersonNotFoundException.class)
        public ResponseEntity<String> personNotFoundExceptionHandler(PersonNotFoundException ex) {
            log.info("cannot find person by username");
            return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(value = AccountIsBannedException.class)
        public ResponseEntity<String> accountIsBannedExceptionHandler(AccountIsBannedException ex) {
            return new ResponseEntity<>(ex.getMessage(), LOCKED);
        }

        @ExceptionHandler(value = InvalidClassException.class)
        public ResponseEntity<String> invalidFileSizeExceptionHandler(InvalidClassException ex) {
            return new ResponseEntity<>(ex.getMessage(), NOT_ACCEPTABLE);
        }

        @ExceptionHandler(value = MaxUploadSizeExceededException.class)
        public ResponseEntity<String> invalidFileSizeExceptionHandler(MaxUploadSizeExceededException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = InvalidFileTypeException.class)
        public ResponseEntity<String> invalidFileTypeExceptionHandler(InvalidFileTypeException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = PartyNotFoundException.class)
        public ResponseEntity<String> partyNotFoundExceptionHandler(PartyNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = IllegalPartyOrganizerException.class)
        public ResponseEntity<String> illegalPartyOrganizerHandler(IllegalPartyOrganizerException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = RequestAlreadyExistException.class)
        public ResponseEntity<String> requestAlreadyExistExceptionHandler(RequestAlreadyExistException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = IllegalDataOfEventException.class)
        public ResponseEntity<String> illegalDateOfEventExceptionHandler(IllegalDataOfEventException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = StatusNotFoundException.class)
        public ResponseEntity<String> statusNotFoundExceptionHandler(StatusNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(value = RequestNotFoundException.class)
        public ResponseEntity<String> requestNotFoundExceptionHandler(RequestNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(value = ParticipationRequestAlsoExists.class)
        public ResponseEntity<String> participationRequestAlsoExistsHandler(ParticipationRequestAlsoExists ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = ParticipationRequestNotFoundException.class)
        public ResponseEntity<String> participationRequestNotFoundExceptionHandler(ParticipationRequestNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(value = InvalidRequestOwner.class)
        public ResponseEntity<String> invalidRequestOwnerHandler(InvalidRequestOwner ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = IOException.class)
        public ResponseEntity<String> iOExceptionHandler(IOException ex) {
            log.error("something get wrong with dropbox", ex);
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = DbxException.class)
        public ResponseEntity<String> dbxExceptionHandler(DbxException ex) {
            log.error("something get wrong with dropbox", ex);
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = UploadErrorException.class)
        public ResponseEntity<String> uploadErrorExceptionHandler(UploadErrorException ex) {
            log.error("something get wrong with dropbox", ex);
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = PartyTypeNotFoundException.class)
        public ResponseEntity<String> partyTypeNotFoundExceptionHandler(PartyTypeNotFoundException ex) {
            log.info("cannot find party type");
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = EmptyPageException.class)
        public ResponseEntity<String> emptyPageExceptionHandler(EmptyPageException ex) {
            log.info("method returns empty page");
            return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(value = UnavailablePageNumberException.class)
        public ResponseEntity<String> unavailablePageNumberExceptionHandler(UnavailablePageNumberException ex) {
            log.info("cannot find page");
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = IllegalRateException.class)
        public ResponseEntity<String> illegalRateExceptionHandler(IllegalRateException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(value = UnauthorizedException.class)
        public ResponseEntity<String> unauthorizedExceptionHandler(UnauthorizedException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }
}
package by.intexsoft.diplom.draft.exception.handler;

import by.intexsoft.diplom.draft.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class DraftExceptionHandler {

        @ExceptionHandler(PartyNotFoundException.class)
        public ResponseEntity<String> partyNotFoundExceptionHandle(PartyNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(DraftStatusNotFoundException.class)
        public ResponseEntity<String> draftStatusNotFoundExceptionHandle(DraftStatusNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(DraftNotFoundException.class)
        public ResponseEntity<String> draftNotFoundExceptionHandle(DraftNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(DraftTypeNotFoundException.class)
        public ResponseEntity<String> draftTypeNotFoundExceptionHandle(DraftTypeNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(PartyStatusNotFoundException.class)
        public ResponseEntity<String> partyStatusNotFoundExceptionHandle(PartyStatusNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(PersonNotFoundException.class)
        public ResponseEntity<String> personNotFoundExceptionHandle(PersonNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(IllegalDraftOwnerException.class)
        public ResponseEntity<String> illegalDraftOwnerExceptionHandle(IllegalDraftOwnerException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(IllegalPartyOwnerException.class)
        public ResponseEntity<String> illegalPartyOwnerExceptionHandle(IllegalPartyOwnerException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> illegalIllegalArgumentExceptionHandle(IllegalArgumentException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(DraftAlreadyExistsException.class)
        public ResponseEntity<String> draftAlreadyExistsExceptionHandle(DraftAlreadyExistsException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }
}
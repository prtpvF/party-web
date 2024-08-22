package by.intexsoft.diplom.chat.exception.handler;

import by.intexsoft.diplom.chat.exception.PersonNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ChatExceptionHandler {

        @ExceptionHandler(PersonNotFoundException.class)
        public ResponseEntity<String> personNotFoundExceptionHandler(PersonNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }
}

package by.intexsoft.diplom.publicapi.exception;

public class PasswordsDontMatchException extends RuntimeException {

        public PasswordsDontMatchException(String message) {
        super(message);
    }
}

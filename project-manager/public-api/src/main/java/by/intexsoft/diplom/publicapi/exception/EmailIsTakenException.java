package by.intexsoft.diplom.publicapi.exception;

public class EmailIsTakenException extends RuntimeException{

    public EmailIsTakenException(String message) {
        super(message);
    }
}

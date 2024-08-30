package by.intexsoft.diplom.auth.exception;

public class TokenHasExpiredException extends RuntimeException {

        public TokenHasExpiredException(String message) {
            super(message);
        }
}

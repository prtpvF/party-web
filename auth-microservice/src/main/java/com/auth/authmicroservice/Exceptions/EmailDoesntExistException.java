package com.auth.authmicroservice.Exceptions;

public class EmailDoesntExistException extends Exception{
    public EmailDoesntExistException(String message) {
        super(message);
    }
}

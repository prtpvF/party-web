package com.by.chaplygin.demo.Exceptions;

public class EmptyPartyListException extends RuntimeException{

    public EmptyPartyListException(String message) {
        super(message);
    }
}

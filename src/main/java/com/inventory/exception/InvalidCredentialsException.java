package com.inventory.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid password or email");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}

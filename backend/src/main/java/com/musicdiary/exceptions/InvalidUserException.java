package com.musicdiary.exceptions;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("Invalid user");
    }
}

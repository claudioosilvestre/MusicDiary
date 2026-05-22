package com.musicdiary.exceptions;

public class PasswordDoesNotMatchException extends MusicDiaryException {

    public PasswordDoesNotMatchException() {
        super("Password does not match");
    }
}

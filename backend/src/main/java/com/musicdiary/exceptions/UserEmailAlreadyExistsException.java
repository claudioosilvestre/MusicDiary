package com.musicdiary.exceptions;

public class UserEmailAlreadyExistsException extends MusicDiaryException{

    public UserEmailAlreadyExistsException() {
        super("Email already exists");
    }
}

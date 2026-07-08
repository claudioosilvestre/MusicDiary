package com.musicdiary.exceptions;

public class UserNotFoundException extends MusicDiaryException {
    
    public UserNotFoundException() {
        super("User not found");
    }
}

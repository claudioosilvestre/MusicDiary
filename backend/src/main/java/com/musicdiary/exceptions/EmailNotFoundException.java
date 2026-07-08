package com.musicdiary.exceptions;

public class EmailNotFoundException extends MusicDiaryException {
    
    public EmailNotFoundException() {
        super("Email not found");
    }
}

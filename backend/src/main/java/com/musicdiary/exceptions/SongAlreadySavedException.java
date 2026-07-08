package com.musicdiary.exceptions;

public class SongAlreadySavedException extends RuntimeException {
    
    public SongAlreadySavedException() {
        super("This song is already saved");
    }
}

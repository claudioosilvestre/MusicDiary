package com.musicdiary.exceptions;

public class SongNotFoundException extends MusicDiaryException {
    
    public SongNotFoundException() {
        super("Song not found");
    }
}

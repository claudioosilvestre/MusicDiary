package com.musicdiary.exceptions;

public class SavedSongNotFoundException extends MusicDiaryException{
    
    public SavedSongNotFoundException() {
        super("Saved song not found");
    }
}

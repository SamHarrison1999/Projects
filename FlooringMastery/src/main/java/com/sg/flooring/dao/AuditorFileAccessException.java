package com.sg.flooring.dao;

public class AuditorFileAccessException extends Exception{
    public AuditorFileAccessException(String message){
        super(message);
    }
    public AuditorFileAccessException(String message, Throwable cause){
        super(message,cause);
    }
}

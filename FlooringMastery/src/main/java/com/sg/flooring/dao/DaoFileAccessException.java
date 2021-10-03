package com.sg.flooring.dao;

public class DaoFileAccessException extends Exception {
    public DaoFileAccessException(String message){
        super(message);
    }
    public DaoFileAccessException(String message, Throwable cause){
        super(message,cause);
    }
}

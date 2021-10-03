package com.sg.flooring.service;

public class DateFormatException extends Exception{
    public DateFormatException(String message){
        super(message);
    }
    public DateFormatException(String message, Throwable cause){
        super(message,cause);
    }
}

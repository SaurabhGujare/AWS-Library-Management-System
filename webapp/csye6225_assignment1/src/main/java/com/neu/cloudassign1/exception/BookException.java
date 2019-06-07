package com.neu.cloudassign1.exception;

public class BookException extends Exception {

    public BookException(String message){ super(message); }

    public BookException(String message,Throwable cause){
        super(cause);
    }
}

package com.neu.cloudassign1.exception;

public class UserException extends Exception {
	
	
	public UserException(String message) {
		super(message);
	}

	public UserException(String message,Throwable cause) {
		super(cause);
	}

}

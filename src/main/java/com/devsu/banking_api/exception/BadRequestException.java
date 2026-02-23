package com.devsu.banking_api.exception;

public class BadRequestException extends RuntimeException{

	public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    /**
	 * 
	 */
	private static final long serialVersionUID = 2709557760996612803L;
}

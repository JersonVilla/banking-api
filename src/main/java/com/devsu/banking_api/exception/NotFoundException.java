package com.devsu.banking_api.exception;

public class NotFoundException extends RuntimeException{
	
	public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    /**
	 * 
	 */
	private static final long serialVersionUID = -9106875877676735674L;
}

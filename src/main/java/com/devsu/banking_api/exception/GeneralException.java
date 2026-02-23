package com.devsu.banking_api.exception;

public class GeneralException extends RuntimeException{
	
	public GeneralException() {
    }

    public GeneralException(String message) {
        super(message);
    }

    /**
	 * 
	 */
	private static final long serialVersionUID = -9037146300999437493L;
}

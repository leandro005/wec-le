package com.lec.wenance.challenge.api.exception;

public class RestApiValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6728356393997552031L;
	
	String key;
	

	public RestApiValidationException(String key, String message) {
		super(message);
		this.key = key;
	}
	
	public RestApiValidationException(String message) {
		super(message);
	}

}

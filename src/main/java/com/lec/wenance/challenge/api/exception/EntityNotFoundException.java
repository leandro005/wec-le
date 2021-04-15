package com.lec.wenance.challenge.api.exception;

public class EntityNotFoundException extends RestApiValidationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5335033308018937899L;

	
	public EntityNotFoundException(String key, String message) {
		super(key, message);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}
	
}

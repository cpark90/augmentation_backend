package com.nexr.de.grnd.exception;

public class EngineException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String engineName;
	
	public EngineException(String message, String engineName) {
		super(message);
		this.engineName = engineName;
	}
	
	public String getEngineName() {
		return this.engineName;
	}
}

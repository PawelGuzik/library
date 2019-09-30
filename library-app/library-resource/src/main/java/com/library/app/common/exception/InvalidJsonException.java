package com.library.app.common.exception;

public class InvalidJsonException extends RuntimeException {

	private static final long serialVersionUID = -1251378421418339718L;
	
	public InvalidJsonException(String message) {
		super(message);
	}
	
	public InvalidJsonException(Throwable throwable) {
		super(throwable);
	}

}

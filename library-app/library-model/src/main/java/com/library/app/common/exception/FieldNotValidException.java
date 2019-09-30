package com.library.app.common.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class FieldNotValidException extends RuntimeException {
	private static final long serialVersionUID = 4525821332583716666L;

	private final String filedName;

	public FieldNotValidException(String filedName, String message) {
		super(message);
		this.filedName = filedName;
	}

	public String getFieldName() {
		return filedName;
	}

	@Override
	public String toString() {
		return "FieldNotValidException [filedName=" + filedName + "]";
	}

}

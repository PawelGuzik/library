package com.library.app.common.model;

public class OperationResult {
	
	private boolean success;
	private String errorIdentyfication;
	private String errorDescription;
	private Object entity;
	
	private OperationResult(final Object entiy) {
		this.success = true;
		this.entity =entiy;
	}
	
	private OperationResult(final String errorIdentification, final String errorDescripton) {
		this.success = false;
		this.errorIdentyfication = errorIdentification;
		this.errorDescription = errorDescripton;
	}
	
	public static OperationResult success(final Object entity) {
		return new OperationResult(entity);
	}
	
	public static OperationResult success() {
		return new OperationResult(null);
	}
	
	public static OperationResult error(final String errorIdentification, final String errorDescription) {
		return new OperationResult(errorIdentification, errorDescription);
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public Object getEntity() {
		return entity;
	}
	
	public String getErrorIdentification() {
		return errorIdentyfication;
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}

	
@Override
public String  toString() {
	return "OperationResult [success=" + success + ", errorIdentification=" + errorIdentyfication
			+ ", errorDescription=" + errorDescription + ", entity=" + entity + "]";
	
}
}

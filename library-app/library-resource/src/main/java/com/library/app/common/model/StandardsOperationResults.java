package com.library.app.common.model;

import com.library.app.common.exception.FieldNotValidException;

public final class StandardsOperationResults {

	private StandardsOperationResults() {
	}
	
	public static OperationResult getOperationResultExistent(final ResourceMessage resourceMessage, final String fieldsNames) {
		return OperationResult.error(resourceMessage.getKeyOfResourcExistent(), resourceMessage.getMessageOfResourceExistent(fieldsNames));
	}
	
	public static OperationResult getOperationInvalidField(final ResourceMessage resourceMessage, final FieldNotValidException ex) {
		return OperationResult.error(resourceMessage.getKeyOfInvalidField(ex.getFieldName()), ex.getMessage());
	}
	
	public static OperationResult getOperationResultNotFound(final ResourceMessage resourceMessage) {
		return OperationResult.error(resourceMessage.getKeyOfResouceNotFound(), resourceMessage.getMessageOfResourceNotFound());
	}
}
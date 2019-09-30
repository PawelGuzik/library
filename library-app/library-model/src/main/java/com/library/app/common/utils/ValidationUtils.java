package com.library.app.common.utils;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.model.Category;
import com.library.app.common.exception.FieldNotValidException;

public class ValidationUtils {

	public static <T> void validateEntityFields(Validator validator, final T entity) {
		final Set<ConstraintViolation<T>> errors = validator.validate(entity);
		final Iterator<ConstraintViolation<T>> itErrors = errors.iterator();
		if(itErrors.hasNext()) {
			final ConstraintViolation<T> violtion = itErrors.next();
			throw new FieldNotValidException(violtion.getPropertyPath().toString(), violtion.getMessage());
			
		}
	}
}

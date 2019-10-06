package com.library.app.user.excpetion;

import javax.ejb.ApplicationException;

@ApplicationException
public class UserNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -6593763676349368689L;

}

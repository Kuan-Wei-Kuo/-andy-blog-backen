package com.andy.blog.exception;

import org.springframework.http.HttpStatus;

public class InvalidJwtAuthenticationException extends RestException {

	private static final long serialVersionUID = -7656640015222368128L;

	public InvalidJwtAuthenticationException(String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}

}

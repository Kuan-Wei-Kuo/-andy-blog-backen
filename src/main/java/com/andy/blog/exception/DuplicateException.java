package com.andy.blog.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends RestException {

	private static final long serialVersionUID = 2604376750576076042L;

	public DuplicateException(String message) {
		super(HttpStatus.CONFLICT, message);
	}

}

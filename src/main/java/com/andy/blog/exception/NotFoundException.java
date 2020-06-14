package com.andy.blog.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestException {

	private static final long serialVersionUID = 3380272603090373443L;

	public NotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

}

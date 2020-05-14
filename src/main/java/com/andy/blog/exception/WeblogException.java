package com.andy.blog.exception;

import org.springframework.http.HttpStatus;

public class WeblogException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private HttpStatus status;

	public WeblogException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	
}

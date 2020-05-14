package com.andy.blog.post.exception;

import org.springframework.http.HttpStatus;

import com.andy.blog.exception.WeblogException;

public class PostException extends WeblogException {

	private static final long serialVersionUID = 1L;

	public PostException(HttpStatus status, String message) {
		super(status, message);
	}

}

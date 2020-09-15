package com.andy.blog.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.andy.blog.model.Message;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RestException.class)
	public ResponseEntity<Message> handleWeblogException(RestException ex, WebRequest req) {
		Message message = new Message();
		message.setStatusCode(ex.getStatus().value());
		message.setMessage(ex.getMessage());
		message.setTimestamp(ZonedDateTime.now());
		return new ResponseEntity<Message>(message, ex.getStatus());
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<Message> handleAuthenticationException(Exception ex) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		Message message = new Message();
		message.setStatusCode(status.value());
		message.setMessage(ex.getMessage());
		message.setTimestamp(ZonedDateTime.now());
		return new ResponseEntity<Message>(message, status);
	}

}

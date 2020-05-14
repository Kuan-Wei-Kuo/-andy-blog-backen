package com.andy.blog.exception;

import java.time.ZonedDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.andy.blog.model.Message;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WeblogException.class)
	public ResponseEntity<Message> handleWeblogException(WeblogException ex, WebRequest req) {
		Message message = new Message();
		message.setStatusCode(ex.getStatus().value());
		message.setMessage(ex.getMessage());
		message.setTimestamp(ZonedDateTime.now());
		return new ResponseEntity<Message>(message, ex.getStatus());
	}
	
}

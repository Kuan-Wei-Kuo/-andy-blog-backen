package com.andy.blog.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Service
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

	@Autowired
	private HandlerExceptionResolver handlerExceptionResolver;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("User is unauthorised. Routing from the entry point");
		handlerExceptionResolver.resolveException(request, response, null, authException);
	}
	
}

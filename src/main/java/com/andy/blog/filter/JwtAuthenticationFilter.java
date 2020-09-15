package com.andy.blog.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.andy.blog.model.ApplicationUser;
import com.andy.blog.model.Token;
import com.andy.blog.provider.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JwtProvider jwtProvider;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			ApplicationUser creds = new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();
		
		String accessToken = jwtProvider.createAccessToken(user.getUsername());
		String refreshToken = jwtProvider.createRefreshToken(user.getUsername());
		
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
        response.getWriter().print(objectMapper.writeValueAsString(new Token(accessToken, refreshToken)));
        response.getWriter().flush();
	}

}

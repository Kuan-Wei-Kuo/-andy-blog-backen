package com.andy.blog.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.andy.blog.provider.JwtProvider;
import com.andy.blog.util.SecurityConstants;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private List<RequestMatcher> requestMatchers = new ArrayList<RequestMatcher>();

	private JwtProvider jwtProvider;
	
	private UserDetailsService userDetailsService;
	
	public JwtAuthorizationFilter(AuthenticationManager authManager, JwtProvider jwtProvider, UserDetailsService userDetailsService) {
		super(authManager);
		this.jwtProvider = jwtProvider;
		this.userDetailsService = userDetailsService;
	}
	
	public JwtAuthorizationFilter ingnoreMatchers(String...patterns) {
		Arrays.stream(patterns).forEach(pattern -> requestMatchers.add(new AntPathRequestMatcher(pattern)));
		return this;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		for(RequestMatcher requestMatcher : requestMatchers) {
			if(requestMatcher.matches(request)) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		
		String authorizationString = request.getHeader(SecurityConstants.HEADER_STRING);
		if(Objects.isNull(authorizationString) || !authorizationString.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			String token = authorizationString.replace(SecurityConstants.TOKEN_PREFIX, "");
			String username = jwtProvider.getUsernameByAccessToken(token);
			if(Objects.nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
				if(jwtProvider.validateAccessToken(token)) {
					System.out.println(username);
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to set user authentication is security context: ", e);
			throw e;
		}
		
		filterChain.doFilter(request, response);
	}

}

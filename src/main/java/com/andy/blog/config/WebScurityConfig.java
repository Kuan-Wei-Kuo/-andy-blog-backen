package com.andy.blog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.andy.blog.filter.JwtAuthenticationFilter;
import com.andy.blog.provider.JwtProvider;

@Configuration
@EnableWebSecurity
public class WebScurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${cors.allowed.origins}")
	private String[] allowedOrigins;
	
	@Value("${cors.allowed.methods}")
	private String[] allowedMethods;

	@Value("${cors.allowed.headers}")
	private String[] allowedHeaders;

	@Value("${cors.exposed.headers}")
	private String[] exposedHeaders;
	
	@Value("${dashboard.username}")
	private String username;

	@Value("${dashboard.username}")
	private String password;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Bean
	public CorsConfigurationSource configurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
		configuration.setAllowedMethods(Arrays.asList(allowedMethods));
		configuration.setAllowedHeaders(Arrays.asList(allowedHeaders));
		configuration.setExposedHeaders(Arrays.asList(exposedHeaders));
		configuration.setAllowCredentials(true);
		configuration.applyPermitDefaultValues();
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", configuration);
		return configurationSource;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser(username)
			.password(passwordEncoder().encode(password));
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors()
				.disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
				.antMatchers("/public/**").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/logout").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.disable()
			.logout()
				.disable()
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtProvider), UsernamePasswordAuthenticationFilter.class);
			.addFilterBefore(filter, beforeFilter);
			
		http.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint);
	}
	
}

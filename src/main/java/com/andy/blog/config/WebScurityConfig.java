package com.andy.blog.config;

import java.util.ArrayList;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.andy.blog.filter.JwtAuthenticationFilter;
import com.andy.blog.filter.JwtAuthorizationFilter;
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

	@Value("${dashboard.password}")
	private String password;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
		configuration.setAllowedMethods(Arrays.asList(allowedMethods));
		configuration.setAllowedHeaders(Arrays.asList(allowedHeaders));
		configuration.setExposedHeaders(Arrays.asList(exposedHeaders));
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", configuration);
		return configurationSource;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(new User(username, passwordEncoder().encode(password), new ArrayList<>()));
		return manager;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inMemoryUserDetailsManager()).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors()
				.and()
			.csrf()
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
			.addFilterAt(new JwtAuthenticationFilter(authenticationManager(), jwtProvider), UsernamePasswordAuthenticationFilter.class)
			.addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtProvider, userDetailsService));
			
		http.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint);
	}
	
}

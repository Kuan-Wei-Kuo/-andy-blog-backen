package com.andy.blog.provider;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.andy.blog.exception.InvalidJwtAuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	private static ConcurrentHashMap<String, String> jtiMap = new ConcurrentHashMap<String, String>();
	
	@Value("${jwt.access.expired}")
	private long accessExpired;
	
	@Value("${jwt.access.secret}")
	private String accessSecret;
	
	@Value("${jwt.access.expired}")
	private long refreshExpired;

	@Value("${jwt.refresh.secret}")
	private String refreshSecret;
	
	public String createAccessToken(String username) {
		String jti = getJti(username);
		Date now = new Date();
		Date expiration = new Date(now.getTime() + accessExpired);
		return Jwts.builder()
			.setId(jti)
			.setSubject(username)
			.setExpiration(expiration)
			.setIssuedAt(now)
			.setNotBefore(now)
			.signWith(SignatureAlgorithm.HS256, accessSecret)
			.compact();
	}
	
	public String createRefreshToken(String username) {
		String jti = getJti(username);
		Date now = new Date();
		Date expiration = new Date(now.getTime() + refreshExpired);
		return Jwts.builder()
			.setId(jti)
			.setSubject(username)
			.setExpiration(expiration)
			.setIssuedAt(now)
			.setNotBefore(now)
			.signWith(SignatureAlgorithm.HS256, refreshSecret)
			.compact();
	}
	
	public String getUsernameByAccessToken(String token) {
        return Jwts.parser().setSigningKey(accessSecret).parseClaimsJws(token).getBody().getSubject();
    }
	
	public String getUsernameByRefreshToken(String token) {
        return Jwts.parser().setSigningKey(refreshSecret).parseClaimsJws(token).getBody().getSubject();
    }
	
	public boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid access jwt token");
        }
    }
	
	public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid refresh jwt token");
        }
    }


	
	private String getJti(String username) {
		String jti = jtiMap.get(username);
		if(Objects.isNull(jti))
			jti = UUID.randomUUID().toString().replace("-", "");
		return jti;
	}
	
}

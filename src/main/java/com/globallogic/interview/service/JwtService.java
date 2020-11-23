package com.globallogic.interview.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	
	private final static int CANTIDAD_HORAS = 10;
	
	private final static String SECRET_KEY = "globallogic";
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	public String generarToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return crearToken(claims, username);
	}
	
	private String crearToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()  + 1000 * 60 * 60 * CANTIDAD_HORAS))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	private boolean tokenVencido(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public Boolean validarToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (userDetails.getUsername().equals(username) && !tokenVencido(token));
	}
}

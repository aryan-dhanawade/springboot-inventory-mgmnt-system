package com.aryan.inventory.security;

import org.springframework.stereotype.Service;

import com.aryan.inventory.config.JwtProperties;

import javax.crypto.SecretKey;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.function.Function;

@Service
public class JwtService {

	private final JwtProperties jwtProperties;

	public JwtService(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	public String generateToken(UserDetails userDetails) {

	    return generateToken(new HashMap<>(), userDetails);

	}
	
	public String generateToken(
	        Map<String, Object> claims,
	        UserDetails userDetails) {

	    return Jwts.builder()

	            .claims(claims)

	            .subject(userDetails.getUsername())

	            .issuedAt(new Date())

	            .expiration(new Date(
	                    System.currentTimeMillis() + jwtProperties.getExpiration()))

	            .signWith(getSigningKey())

	            .compact();
	}
	

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> resolver) {

		Claims claims = extractAllClaims(token);

		return resolver.apply(claims);
	}


	public boolean isTokenValid(String token, UserDetails userDetails) {

		String username = extractUsername(token);

		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	private Claims extractAllClaims(String token) {

		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	private boolean isTokenExpired(String token) {

		return extractExpiration(token).before(new Date());
	}


	private SecretKey getSigningKey() {

		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());

		return Keys.hmacShaKeyFor(keyBytes);
	}

}
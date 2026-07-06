package com.aryan.inventory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="jwt")
public class JwtProperties {
	
	private String secret;
	private long expiration;
	
	
	public JwtProperties(String secret, long expiration) {
		this.secret = secret;
		this.expiration = expiration;
	}


	public String getSecret() {
		return secret;
	}


	public long getExpiration() {
		return expiration;
	}
	


}

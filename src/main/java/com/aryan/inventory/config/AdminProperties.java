package com.aryan.inventory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "admin")
public class AdminProperties {
	
	private String username;
	private String password;
	
	public AdminProperties(String username, String password) {
		
		this.username = username;
		this.password = password;
		
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	
	
	

}

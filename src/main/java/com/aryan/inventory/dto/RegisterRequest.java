package com.aryan.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
	
	@NotBlank
	@Size(min = 3, max = 30, message = "Username must be between 3 to 30 charecters")
    private String username;
	
	@NotBlank
	@Size(min = 8, max = 100)
	@Pattern(
		    regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
		    message = "Password must contain uppercase, lowercase and a number."
		)
    private String password;
	
    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
package com.example.usermanagement.Dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignInResponse {
    @Min(value = 1, message = "User ID should be greater than 0")
    private Integer userId;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "ADMIN|USER", message = "Role should be either 'ADMIN' or 'USER'")
    private String role;

    private String token;
    private String refreshToken;
    private String expirationTime;

    // Constructors, getters, and setters
}


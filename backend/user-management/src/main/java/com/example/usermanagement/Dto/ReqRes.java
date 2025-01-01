package com.example.usermanagement.Dto;


import com.example.usermanagement.Entity.OurUsers;
import com.example.usermanagement.Entity.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "ADMIN|USER", message = "Role should be either 'ADMIN' or 'USER'")
    private String role;

    @Min(value = 1, message = "User ID should be greater than 0")
    private Integer userId;

    private String password;
    private OurUsers ourUsers;
    private UserProfile userProfile;
    private String verificationCode;
    private String newPassword;
    private String accStatus;
    private List<UserProfile> userProfiles;

    private Object data;
 }

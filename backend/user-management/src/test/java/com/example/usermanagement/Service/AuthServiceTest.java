package com.example.usermanagement.Service;

import com.example.usermanagement.Dto.ChangePasswordReq;
import com.example.usermanagement.Dto.ReqRes;
import com.example.usermanagement.Entity.OurUsers;
import com.example.usermanagement.Repository.OurUserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Disabled
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private OurUserRepo ourUserRepo;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailSenderService emailSenderService;


    private OurUsers mockUser;
    private ReqRes request;
    private ReqRes response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new OurUsers();
        mockUser.setId(1);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
        mockUser.setVerificationCode("123456");

        request = new ReqRes();
        request.setEmail("test@example.com");
        request.setPassword("123@abc");

        response = new ReqRes();
    }

    @Test
    public void testSignUp() {
        request.setRole("USER");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(ourUserRepo.save(any(OurUsers.class))).thenReturn(mockUser);

        ReqRes result = authService.signUp(request);

        assertEquals(200, result.getStatusCode());
        assertEquals("User Saved Successfully", result.getMessage());
        verify(emailSenderService, times(1)).sendEmail(eq("test@example.com"), eq("Email Verification Code"), anyString());
    }

    @Test
    public void testSignIn() {
        HashMap<String, Object> claims = new HashMap<>();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(ourUserRepo.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtUtils.generateToken(claims, mockUser)).thenReturn("mockJwtToken");
        when(jwtUtils.generateRefreshToken(any(), eq(mockUser))).thenReturn("mockRefreshToken");

        ReqRes result = authService.signIn(request);

        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully Signed In", result.getMessage());
        assertNotNull(result.getToken());
        assertNotNull(result.getRefreshToken());
    }

    @Test
    public void testRefreshToken() {
        HashMap<String, Object> claims = new HashMap<>();
        request.setToken("mockRefreshToken");
        when(jwtUtils.extractUsername(anyString())).thenReturn("test@example.com");
        when(ourUserRepo.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(jwtUtils.isTokenValid(anyString(), any(OurUsers.class))).thenReturn(true);
        when(jwtUtils.generateToken(claims, any(OurUsers.class))).thenReturn("newJwtToken");

        ReqRes result = authService.refreshToken(request);

        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully Refreshed Token", result.getMessage());
        assertEquals("newJwtToken", result.getToken());
    }

    @Test
    public void testChangePassword() {
        ChangePasswordReq changePasswordRequest = new ChangePasswordReq();
        changePasswordRequest.setCurrentPassword("currentPassword");
        changePasswordRequest.setNewPassword("newPassword");

        when(ourUserRepo.findById(anyInt())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        ReqRes result = authService.changePassword(1, changePasswordRequest);

        assertEquals(200, result.getStatusCode());
        assertEquals("Password Changed Successfully", result.getMessage());
        verify(ourUserRepo, times(1)).save(mockUser);
    }

    @Test
    public void testForgotPassword() {
        when(ourUserRepo.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        ReqRes result = authService.forgotPassword(request);

        assertEquals(200, result.getStatusCode());
        assertEquals("Password Reset Code Sent to Email", result.getMessage());
        verify(emailSenderService, times(1)).sendEmail(eq("test@example.com"), eq("Password Reset Code"), anyString());
    }

    @Test
    public void testForgotPasswordVerify() {
        request.setVerificationCode("123456");
        request.setNewPassword("newPassword");

        when(ourUserRepo.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        ReqRes result = authService.forgotPasswordVerify(request);

        assertEquals(200, result.getStatusCode());
        assertEquals("Password Reset Successfully", result.getMessage());
        verify(ourUserRepo, times(1)).save(mockUser);
    }

    @Test
    public void testVerifyEmail() {
        request.setVerificationCode("123456");

        when(ourUserRepo.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        ReqRes result = authService.verifyEmail(request);

        assertEquals(200, result.getStatusCode());
        assertEquals("Email Verified Successfully", result.getMessage());
        verify(ourUserRepo, times(1)).save(mockUser);
    }


}

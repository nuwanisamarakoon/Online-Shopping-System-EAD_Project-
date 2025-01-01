package com.example.usermanagement.Controller;

import com.example.usermanagement.Dto.ReqRes;
import com.example.usermanagement.Service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Disabled
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private ReqRes request;
    private ReqRes response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new ReqRes();
        response = new ReqRes();

        request.setEmail("test@example.com");
        request.setPassword("123@abc");

        response.setMessage("Success");
    }

    @Test
    void testSignUp() {

        when(authService.signUp(request)).thenReturn(response );

        ResponseEntity<ReqRes> result = authController.signUp(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User Saved Successfully", result.getBody().getMessage());
    }

    @Test
    void testSignIn() {

        when(authService.signIn(request)).thenReturn(response);

        ResponseEntity<ReqRes> result = authController.signIn(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("jwtToken", result.getBody().getToken());
    }

    @Test
    void testRefreshToken() {

        when(authService.refreshToken(request)).thenReturn(response);

        ResponseEntity<ReqRes> result = authController.refreshToken(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Successfully Refreshed Token", result.getBody().getMessage());
    }
}

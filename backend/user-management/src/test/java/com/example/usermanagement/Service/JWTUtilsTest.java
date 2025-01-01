package com.example.usermanagement.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled
public class JWTUtilsTest {

    @InjectMocks
    private JWTUtils jwtUtils;

    @Mock
    private UserDetails userDetails;

    private SecretKey key;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        String secretString = "";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");

        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    void testGenerateToken() {
        HashMap<String, Object> claims = new HashMap<>();
        String token = jwtUtils.generateToken(claims, userDetails);

        assertNotNull(token);

        String username = jwtUtils.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testGenerateTokenRefresh() {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        String refreshToken = jwtUtils.generateRefreshToken(claims, userDetails);

        assertNotNull(refreshToken);

        Claims extractedClaims = Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        assertEquals("testUser", extractedClaims.getSubject());
        assertEquals("USER", extractedClaims.get("role"));
    }

    @Test
    void testExtractUsername() {
        HashMap<String, Object> claims = new HashMap<>();

        String token = jwtUtils.generateToken(claims, userDetails);
        String username = jwtUtils.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void testIsTokenValid() {
        HashMap<String, Object> claims = new HashMap<>();

        String token = jwtUtils.generateToken(claims, userDetails);
        assertTrue(jwtUtils.isTokenValid(token, userDetails));

        UserDetails differentUser = mock(UserDetails.class);
        when(differentUser.getUsername()).thenReturn("differentUser");
        assertFalse(jwtUtils.isTokenValid(token, differentUser));
    }

    @Test
    void testIsTokenExpired() {
        HashMap<String, Object> claims = new HashMap<>();

        String token = jwtUtils.generateToken(claims, userDetails);

        assertFalse(jwtUtils.isTokenExpired(token));

        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(key)
                .compact();

        assertTrue(jwtUtils.isTokenExpired(expiredToken));

    }
}

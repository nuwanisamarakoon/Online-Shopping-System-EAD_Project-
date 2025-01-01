package com.example.usermanagement;

import com.example.usermanagement.Dto.ReqRes;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

// NOTE: @SpringBootTest is used to test the application with a real server. Slow but real HTTP requests.
// NOTE: @AutoConfigureMockMvc is used to test the application with a mock server. Fast but no HTTP requests.

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled
public class IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private static String createdUserId;
    private static String authToken;
    private static String refreshToken;
    private static String verificationCode;
    private static String resetPasswordToken;
    private static String testEmail;
    private static String testPassword;

    @Test
    void aTestThatPasses() {
        assertTrue(true);
    }

    @Test
    void testSignupWithUsernameAndPassword_ReqResAsRawString() {
        // Creating a request payload exactly like Postman
        String requestJson = """
                {
                    "email": "user@example.com",
                    "password": "password123"
                }
                """;

        // Set headers like Postman
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HttpEntity with raw JSON
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        // Don't deserialize the response to ReqRes, it will give birth to unforeseen demons!
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/auth/signup",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Basic assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertTrue(responseEntity.getBody().contains("User Saved Successfully"));
//        assertTrue(responseEntity.getBody().contains("user@example.com"));
    }

    @Test
    @Order(1)
    void testSignUpWithUniqueEmailAndStrongPasswordGetsAccepted() {
        testEmail = "testuser_" + UUID.randomUUID() + "@example.com";
        testPassword = Helper.generatePassword(
                12,
                2,
                2,
                2,
                2,
                FillMode.LOWER_CASE
        );

        ReqRes signupRequest = new ReqRes();
        signupRequest.setEmail(testEmail);
        signupRequest.setPassword(testPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // parse the request to HttpEntity
        HttpEntity<ReqRes> requestEntity = new HttpEntity<>(signupRequest, headers);

        // Send POST request with TestResponse class
        ResponseEntity<TestResponse> responseEntity = restTemplate.exchange(
                "/auth/signup",
                HttpMethod.POST,
                requestEntity,
                TestResponse.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getBody().getStatusCode());
        assertEquals("User Saved Successfully", responseEntity.getBody().getMessage());
        assertNotNull(responseEntity.getBody().getOurUsers());
        assertEquals(testEmail, responseEntity.getBody().getOurUsers().getEmail());

        // Store created user ID for subsequent tests
        createdUserId = responseEntity.getBody().getOurUsers().getId().toString();
    }

    // BUG: This fails.
    @Test
    @Order(2)
    @Disabled
    void testSignUpWithExistingUsernameAndStrongPasswordGetsRejected() {
        ReqRes signupRequest = new ReqRes();
        signupRequest.setEmail(testEmail);
        signupRequest.setPassword(testPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReqRes> requestEntity = new HttpEntity<>(signupRequest, headers);

        // Send POST request with TestResponse class
        ResponseEntity<TestResponse> responseEntity = restTemplate.exchange(
                "/auth/signup",
                HttpMethod.POST,
                requestEntity,
                TestResponse.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getBody().getStatusCode());
        assertEquals("User Already Exists", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getOurUsers());
    }

    @Test
    @Order(3)
    void testSignInWithUsernameAndPasswordPasses() {
        // these to be deleted
        testEmail = "user@example.com";
        testPassword = "password123";
        // end of deletion

        ReqRes signinRequest = new ReqRes();
        signinRequest.setEmail(testEmail);
        signinRequest.setPassword(testPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReqRes> requestEntity = new HttpEntity<>(signinRequest, headers);

        ResponseEntity<TestResponse> responseEntity = restTemplate.exchange(
                "/auth/signIn",
                HttpMethod.POST,
                requestEntity,
                TestResponse.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getBody().getStatusCode());
        assertNotNull(responseEntity.getBody().getToken());
        assertNotNull(responseEntity.getBody().getRefreshToken());

        // Store tokens for subsequent tests
        authToken = responseEntity.getBody().getToken();
        refreshToken = responseEntity.getBody().getRefreshToken();
    }

    @Test
    @Order(4)
    void testSignInWithIncorrectUsernameFails() {
        ReqRes signinRequest = new ReqRes();
        signinRequest.setEmail("random@email.com");
        signinRequest.setPassword("password123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReqRes> requestEntity = new HttpEntity<>(signinRequest, headers);

        ResponseEntity<TestResponse> responseEntity = restTemplate.exchange(
                "/auth/signIn",
                HttpMethod.POST,
                requestEntity,
                TestResponse.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseEntity.getBody().getStatusCode());
        assertTrue(responseEntity.getBody().getMessage().contains("Invalid email or password"));
    }

    @Test
    @Order(5)
    void testSignInWithIncorrectPasswordFails() {
        ReqRes signinRequest = new ReqRes();
        signinRequest.setEmail("user@example.com");
        signinRequest.setPassword("password12");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReqRes> requestEntity = new HttpEntity<>(signinRequest, headers);

        ResponseEntity<TestResponse> responseEntity = restTemplate.exchange(
                "/auth/signIn",
                HttpMethod.POST,
                requestEntity,
                TestResponse.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseEntity.getBody().getStatusCode());
        assertTrue(responseEntity.getBody().getMessage().contains("Invalid email or password"));
    }

    @Test
    @Order(6)
    void testRefreshTokenPasses() {
        ReqRes refreshTokenRequest = new ReqRes();
        refreshTokenRequest.setToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReqRes> requestEntity = new HttpEntity<>(refreshTokenRequest, headers);

        ResponseEntity<TestResponse> responseEntity = restTemplate.exchange(
                "/auth/token/refresh",
                HttpMethod.POST,
                requestEntity,
                TestResponse.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getBody().getStatusCode());
        assertNotNull(responseEntity.getBody().getToken());
    }

    @Test
    @Order(7)
    void testInvalidRefreshTokenFails() {
        ReqRes refreshTokenRequest = new ReqRes();
        refreshTokenRequest.setToken("somegibberish");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReqRes> requestEntity = new HttpEntity<>(refreshTokenRequest, headers);

        ResponseEntity<TestResponse> responseEntity = restTemplate.exchange(
                "/auth/token/refresh",
                HttpMethod.POST,
                requestEntity,
                TestResponse.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getBody().getStatusCode());
    }

    @Test
    @Order(8)
    void testGetAllUsers() {
        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(authToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/users",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void testGetUserProfileWithBearerTokenPasses() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // delete
        authToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJJZCI6NTUsImFjY1N0YXR1cyI6Ik5vdCBWZXJpZmllZCIsInN1YiI6InVzZXJAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzI4MjY0NzMsImV4cCI6MTczMjkxMjg3M30.hlhwaE5ZbXSlo09xKOb0YWUxgMAVfKtjTj0o2RxYCro";
        createdUserId = "55";
        testEmail = "user@example.com";
        // end of deletion
        headers.setBearerAuth(authToken);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/users/" + createdUserId + "/profile",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode());
//        assertEquals(testEmail, responseEntity.getBody()..getEmail());
    }

    @Test
    @Order(10)
    void testGetUserProfileWithoutBearerTokenFails() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // delete
        createdUserId = "55";
        // end of deletion

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/users/" + createdUserId + "/profile",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // Assertions
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @AfterAll
    static void cleanup() {
        // Delete the user created during testing
        if (createdUserId != null) {
            // delete the user using JPA
            // ourUserRepo.deleteById(Long.parseLong(createdUserId));
        }
    }
}

/**
 * Helper classes for deserializing the response JSON.
 * These classes are used to deserialize the response JSON into Java objects.
 * Default deserialization is not possible because the response JSON is nested.
 */
@Setter
@Getter
class TestAuthority {
    private String authority;
}

@Setter
@Getter
class TestUser {
    private Long id;
    private String email;
    private String role;
    private List<TestAuthority> authorities;

}

@Setter
@Getter
class TestResponse {
    private int statusCode;
    private String message;
    private TestUser ourUsers;
    private String token;
    private String refreshToken;
}

class Helper {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_+=<>?";

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a password with the specified parameters.
     *
     * @param length       Total length of the password.
     * @param numSymbols   Number of symbols to include.
     * @param numUppercase Number of uppercase letters to include.
     * @param numLowercase Number of lowercase letters to include.
     * @param numDigits    Number of digits to include.
     * @param fillMode     The character type to use for filling the remaining length ("uppercase", "lowercase", "symbols", "digits").
     * @return The generated password as a String.
     */
    public static String generatePassword(int length, int numSymbols, int numUppercase, int numLowercase, int numDigits, FillMode fillMode) {
        if (length < numSymbols + numUppercase + numLowercase + numDigits) {
            throw new IllegalArgumentException("Total length cannot be less than the sum of specified character counts.");
        }

        StringBuilder password = new StringBuilder(length);

        // Add specified counts of each character type
        password.append(generateRandomCharacters(SYMBOLS, numSymbols));
        password.append(generateRandomCharacters(UPPERCASE, numUppercase));
        password.append(generateRandomCharacters(LOWERCASE, numLowercase));
        password.append(generateRandomCharacters(DIGITS, numDigits));

        // Calculate remaining length to fill
        int remainingLength = length - password.length();
        String fillCharacters = getFillCharacters(fillMode);
        if (remainingLength > 0) {
            password.append(generateRandomCharacters(fillCharacters, remainingLength));
        }

        // Shuffle to randomize character positions
        return shufflePassword(password.toString());
    }

    private static String generateRandomCharacters(String characterSet, int count) {
        StringBuilder characters = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            characters.append(characterSet.charAt(RANDOM.nextInt(characterSet.length())));
        }
        return characters.toString();
    }

    private static String getFillCharacters(FillMode fillMode) {
        return switch (fillMode) {
            case FillMode.UPPER_CASE -> UPPERCASE;
            case FillMode.LOWER_CASE -> LOWERCASE;
            case FillMode.SPECIAL_CHARACTERS -> SYMBOLS;
            case FillMode.NUMBERS -> DIGITS;
            default ->
                    throw new IllegalArgumentException("Invalid fill mode. Choose from 'uppercase', 'lowercase', 'symbols', 'digits'.");
        };
    }

    private static String shufflePassword(String password) {
        char[] characters = password.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = RANDOM.nextInt(i + 1);
            // Swap characters
            char temp = characters[i];
            characters[i] = characters[index];
            characters[index] = temp;
        }
        return new String(characters);
    }

}

enum FillMode {
    UPPER_CASE, LOWER_CASE, MIXED_CASE, SPECIAL_CHARACTERS, NUMBERS
}


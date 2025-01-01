package com.example.usermanagement.Controller;

import com.example.usermanagement.Dto.ReqRes;
import com.example.usermanagement.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for user signup.
     *
     * @param signUpRequest the request body containing user signup details:
     *                      - email: the user's email address
     *                      - password: the user's password
     * @return ResponseEntity containing the signup response
     */
    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
        ReqRes response = authService.signUp(signUpRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    /**
     * Endpoint for user signIn.
     *
     * @param signInRequest the request body containing user signIn details:
     *                      - email: the user's email address
     *                      - password: the user's password
     * @return ResponseEntity containing the signIn response
     */
    @PostMapping("/signIn")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
        ReqRes response = authService.signIn(signInRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    /**
     * Endpoint for refreshing the authentication token.
     *
     * @param refreshTokenRequest the request body containing the refresh token details:
     *                            - refreshToken: the refresh token
     * @return ResponseEntity containing the refresh token response
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        ReqRes response = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint for verifying the authentication token.
     *
     * @param verifyTokenRequest the request body containing the token to verify
     * @return ResponseEntity containing the verify token response
     */
    @PostMapping("/verify-token")
    public ResponseEntity<ReqRes> verifyToken(@RequestBody ReqRes verifyTokenRequest){
        System.out.println("verify token");
        ReqRes response = authService.verifyToken(verifyTokenRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}

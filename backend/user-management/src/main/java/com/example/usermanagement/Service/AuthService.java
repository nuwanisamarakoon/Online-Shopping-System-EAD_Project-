package com.example.usermanagement.Service;

import com.example.usermanagement.Dto.ChangePasswordReq;
import com.example.usermanagement.Dto.ReqRes;
import com.example.usermanagement.Entity.OurUsers;
import com.example.usermanagement.Entity.UserProfile;
import com.example.usermanagement.Repository.OurUserRepo;
import com.example.usermanagement.Repository.UserProfileRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.HashMap;


@Service
public class AuthService {
    private final OurUserRepo ourUserRepo;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailSenderService;
    private final UserProfileRepo userProfileRepo;

    public AuthService(
            OurUserRepo ourUserRepo,
            JWTUtils jwtUtils,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailSenderService emailSenderService,
            UserProfileRepo userProfileRepo
            ) {
        this.ourUserRepo = ourUserRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailSenderService = emailSenderService;
        this.userProfileRepo = userProfileRepo;
    }

    public ReqRes signUp(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            // Check if the email already exists
            if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email already exists");
                return resp;
            }

            OurUsers ourUsers = new OurUsers();
            UserProfile userProfile = new UserProfile();

            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole("USER");

            // Generate a random verification code
            String verificationCode = generateVerificationCode();
            ourUsers.setVerificationCode(verificationCode);
            ourUsers.setIsVerified(false);

            OurUsers ourUserResult = ourUserRepo.save(ourUsers);

            // Save to the profile
            userProfile.setUser(ourUsers);
            userProfileRepo.save(userProfile);

            if (ourUserResult != null && ourUserResult.getId() > 0) {
                // Send the verification code to the user's email
                emailSenderService.sendEmail(ourUsers.getEmail(), "Email Verification Code", "Your verification code is: " + verificationCode);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(500);
            resp.setError("An error occurred while saving the user");
        }
        return resp;
    }


    public ReqRes signIn(ReqRes signinRequest) {
        ReqRes response = new ReqRes();

        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
            );

            // Fetch user details
            var user = ourUserRepo.findByEmail(signinRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + signinRequest.getEmail()));

            // Add claims for token generation
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("role", user.getRole());
            claims.put("accStatus", user.getIsVerified() ? "Verified" : "Not Verified");

            // Generate tokens
            var jwt = jwtUtils.generateToken(claims, user);
            var refreshToken = jwtUtils.generateRefreshToken(claims, user);


            // Set response fields
            response.setStatusCode(200);
            response.setMessage("Successfully Signed In");
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");

            // Set user-specific details in ReqRes object
            response.setUserId(user.getId());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole());

        } catch (BadCredentialsException ex) {
            response.setStatusCode(401);
            response.setMessage("Invalid email or password");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("An error occurred in the sign-in process");
        }

        return response;
    }


    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            System.out.println("refresh token:" + refreshTokenRequest.getToken());
            OurUsers users = ourUserRepo.findByEmail(ourEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + ourEmail));
            HashMap<String, Object> claims = new HashMap<>();

            // add
            claims.put("userId", users.getId());
            claims.put("role", users.getRole());
            claims.put("accStatus", users.getIsVerified() ? "Verified" : "Not Verified");

            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(claims, users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            } else {
                response.setStatusCode(401);
                response.setMessage("Invalid Refresh Token");
            }
        } catch (UsernameNotFoundException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("An error occurred while refreshing the token");
        }
        return response;
    }

    // changePassword method
    public ReqRes changePassword(Integer id , ChangePasswordReq changePasswordRequest){
        ReqRes response = new ReqRes();
        try {
            OurUsers ourUsers = ourUserRepo.findById(id).orElseThrow();
            if (passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), ourUsers.getPassword())) {
                ourUsers.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                ourUserRepo.save(ourUsers);
                response.setStatusCode(200);
                response.setMessage("Password Changed Successfully");
            }else {
                response.setStatusCode(500);
                response.setMessage("Current Password is Incorrect");
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("An error occurred while changing the password");
        }
        return response;
    }


    public ReqRes forgotPassword(ReqRes forgotPasswordReq) {
        ReqRes response = new ReqRes();
        try {
            OurUsers ourUsers = ourUserRepo.findByEmail(forgotPasswordReq.getEmail()).orElseThrow();
            if (ourUsers != null) {
                // Generate a random verification code
                String verificationCode = generateVerificationCode();

                // Save the verification code in the OurUsers entity
                ourUsers.setVerificationCode(verificationCode);
                ourUserRepo.save(ourUsers);




                // Send the verification code to the user's email
                emailSenderService.sendEmail(ourUsers.getEmail(), "Password Reset Code", "Your Password Reset Code is: " + verificationCode);
                response.setStatusCode(200);
                response.setMessage("Password Reset Code Sent to Email");
            } else {
                response.setStatusCode(500);
                response.setMessage("Email Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("An error occurred while sending the password reset code");
        }
        return response;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate a 6-digit random number
        return String.valueOf(code);
    }

    public ReqRes forgotPasswordVerify(ReqRes reqRes) {
        ReqRes response = new ReqRes();
        try {
            OurUsers ourUsers = ourUserRepo.findByEmail(reqRes.getEmail()).orElseThrow();
            if (ourUsers.getVerificationCode().equals(reqRes.getVerificationCode())) {
                //delete the verification code
                ourUsers.setVerificationCode(null);
                ourUsers.setPassword(passwordEncoder.encode(reqRes.getNewPassword()));
                ourUserRepo.save(ourUsers);
                response.setStatusCode(200);
                response.setMessage("Password Reset Successfully");
            } else {
                response.setStatusCode(401);
                response.setMessage("Verification Code is Incorrect");
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("An error occurred while resetting the password");
        }
        return response;

    }

    public ReqRes verifyEmail(ReqRes reqRes) {
        ReqRes response = new ReqRes();
        try {
            OurUsers ourUsers = ourUserRepo.findByEmail(reqRes.getEmail()).orElseThrow();
            if (ourUsers.getVerificationCode().equals(reqRes.getVerificationCode())) {
                ourUsers.setVerificationCode(null);
                ourUsers.setIsVerified(true);
                ourUserRepo.save(ourUsers);
                response.setStatusCode(200);
                response.setMessage("Email Verified Successfully");
            } else {
                System.out.println(ourUsers.getVerificationCode());
                System.out.println(reqRes.getVerificationCode());
                response.setStatusCode(401);
                response.setMessage("Verification Code is Incorrect");
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("An error occurred while verifying the email");
        }
        return response;
    }

    //create method for verify token
    public ReqRes verifyToken(ReqRes verifyTokenRequest){
        ReqRes response = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(verifyTokenRequest.getToken());
            OurUsers users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(verifyTokenRequest.getToken(), users)) {
                response.setStatusCode(200);
                response.setMessage("Token is Valid");
                response.setUserId(users.getId());
                response.setRole(users.getRole());
                response.setAccStatus(users.getIsVerified() ? "Verified" : "Not Verified");
            } else {
                response.setStatusCode(401);
                response.setMessage("Token is Invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("An error occurred while verifying the token");
        }
        System.out.println(response);
        return response;
    }
}

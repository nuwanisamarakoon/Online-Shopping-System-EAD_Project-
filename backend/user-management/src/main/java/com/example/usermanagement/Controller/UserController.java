package com.example.usermanagement.Controller;

import com.example.usermanagement.Dto.ChangePasswordReq;
import com.example.usermanagement.Dto.ReqRes;
import com.example.usermanagement.Entity.OurUsers;
import com.example.usermanagement.Entity.UserProfile;
import com.example.usermanagement.Service.AuthService;
import com.example.usermanagement.Service.OurUserDetailsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

    private final OurUserDetailsService ourUserDetailsService;
    private final AuthService authService;

    public UserController(AuthService authService, OurUserDetailsService ourUserDetailsService) {
        this.authService = authService;
        this.ourUserDetailsService = ourUserDetailsService;
    }

    /**
     * Validates if the user is authorized to perform the action.
     *
     * This method checks if the user making the request is authorized to perform the action
     * based on their user ID and role. If the user is not authorized, it returns a
     * ResponseEntity containing a ReqRes object with a 401 status code and an appropriate
     * error message. If the user is authorized, it returns null.
     *
     * @param id the ID of the resource
     * @param userId the ID of the user making the request
     * @param role the role of the user making the request
     * @return ResponseEntity containing the validation response if unauthorized, otherwise null
     *         - If unauthorized:
     *           - Status code: 401
     *           - Body: ReqRes object with the following fields set:
     *             - statusCode: 401
     *             - message: "User is not authorized to perform this action"
     *         - If authorized:
     *           - null
     */
    private ResponseEntity<ReqRes> validateAndRespond(Integer id, Integer userId, String role) {
        ReqRes resp = new ReqRes();
        if (!id.equals(userId) && !"ADMIN".equals(role)) {
            resp.setStatusCode(401);
            resp.setMessage("User is not authorized to perform this action");
            return ResponseEntity.status(resp.getStatusCode()).body(resp);
        }
        return null;
    }

    /**
     * Retrieves all user profiles.
     *
     * @param userId the ID of the user making the request
     * @param role the role of the user making the request
     * @return ResponseEntity containing all user profiles
     *         - Status code: 200
     *         - Body: ReqRes object with the following fields set:
     *           - statusCode: 200
     *           - data: List of UserProfile objects
     */
    @GetMapping
    public ResponseEntity<ReqRes> getAllUserProfiles(@RequestParam Integer userId, @RequestParam String role) {
        ReqRes resp = ourUserDetailsService.getAllUserProfiles();
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    /**
     * Retrieves a user profile by ID.
     *
     * @param id the ID of the user profile to retrieve
     * @param userId the ID of the user making the request
     * @param role the role of the user making the request
     * @return ResponseEntity containing the user profile
     *         - Status code: 200
     *         - Body: ReqRes object with the following fields set:
     *           - statusCode: 200
     *           - data: UserProfile object
     */
    @GetMapping("/{id}/profile")
    public ResponseEntity<ReqRes> getUserProfileById(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String role) {
        ResponseEntity<ReqRes> validationResp = validateAndRespond(id, userId, role);
        if (validationResp != null) {
            return validationResp;
        }
        ReqRes resp = ourUserDetailsService.getUserProfileById(id);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }





    /**
     * Creates a new user profile.
     *
     * @param id the ID of the user profile to create
     * @param userId the ID of the user making the request
     * @param role the role of the user making the request
     * @param userProfile the user profile data
     * @param profilePicture the profile picture file
     * @return ResponseEntity containing the created user profile
     *         - Status code: 201
     *         - Body: ReqRes object with the following fields set:
     *           - statusCode: 201
     *           - message: "User profile created successfully"
     */
    @PostMapping("/{id}/profile")
    public ResponseEntity<ReqRes> createUserProfile(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String role, @RequestPart("userProfile") UserProfile userProfile, @RequestPart("profilePicture") MultipartFile profilePicture) {
        ResponseEntity<ReqRes> validationResp = validateAndRespond(id, userId, role);
        if (validationResp != null) {
            return validationResp;
        }
        ReqRes resp = ourUserDetailsService.createUserProfile(id, userProfile, profilePicture);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    /**
     * Updates an existing user profile.
     *
     * @param id the ID of the user profile to update
     * @param userId the ID of the user making the request
     * @param role the role of the user making the request
     * @param userProfileDetails the updated user profile data
     * @param profilePicture the updated profile picture
     * @return ResponseEntity containing the updated user profile
     *         - Status code: 200
     *         - Body: ReqRes object with the following fields set:
     *           - statusCode: 200
     *           - message: "User profile updated successfully"
     */
    @PutMapping(value = "/{id}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReqRes> updateUserProfile(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String role, @RequestPart("userProfileDetails") UserProfile userProfileDetails, @RequestPart("profilePicture") MultipartFile profilePicture) {
        ResponseEntity<ReqRes> validationResp = validateAndRespond(id, userId, role);
        if (validationResp != null) {
            return validationResp;
        }
        ReqRes resp = ourUserDetailsService.updateUserProfile(id, userProfileDetails, profilePicture);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    // for octet-stream
    @PutMapping(value = "/{id}/profile", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ReqRes> updateUserProfileOctet(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String role, @RequestPart("userProfileDetails") UserProfile userProfileDetails, @RequestPart("profilePicture") MultipartFile profilePicture) {
        ResponseEntity<ReqRes> validationResp = validateAndRespond(id, userId, role);
        if (validationResp != null) {
            return validationResp;
        }
        ReqRes resp = ourUserDetailsService.updateUserProfile(id, userProfileDetails, profilePicture);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    /**
     * Deletes a user profile.
     *
     * @param id the ID of the user profile to delete
     * @param userId the ID of the user making the request
     * @param role the role of the user making the request
     * @return ResponseEntity indicating the result of the deletion
     *         - Status code: 204
     *         - message: "User profile deleted successfully"
     */
    @DeleteMapping("/{id}/profile")
    public ResponseEntity<ReqRes> deleteUserProfile(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String role) {
        ResponseEntity<ReqRes> validationResp = validateAndRespond(id, userId, role);
        if (validationResp != null) {
            return validationResp;
        }
        ReqRes resp = ourUserDetailsService.deleteUserProfile(id);
        return ResponseEntity.status(resp.getStatusCode()).build();
    }

    /**
     * Changes the password of a user.
     *
     * @param id the ID of the user whose password is to be changed
     * @param userId the ID of the user making the request
     * @param role the role of the user making the request
     * @param changePasswordReq the password change request data
     * @return ResponseEntity containing the result of the password change
     *         - statusCode: 200
     *         - message: "Password changed successfully"
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<ReqRes> changePassword(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String role, @RequestBody ChangePasswordReq changePasswordReq) {
        ResponseEntity<ReqRes> validationResp = validateAndRespond(id, userId, role);
        if (validationResp != null) {
            return validationResp;
        }
        ReqRes resp = authService.changePassword(id, changePasswordReq);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    /**
     * Verifies the email of a user.
     *
     * @param reqRes the email verification request data
     * @return ResponseEntity containing the result of the email verification
     *          - statusCode: 200
     *          - message: "Email verified successfully"
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ReqRes> verifyEmail(@RequestBody ReqRes reqRes){
        ReqRes resp = authService.verifyEmail(reqRes);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    /**
     * Updates the role of a user.
     *
     * @param id the ID of the user whose role is to be updated
     * @param userId the ID of the user making the request
     * @param role the role of the user making the request
     * @param newRole the new role to be assigned to the user
     * @return ResponseEntity containing the result of the role update
     *         - statusCode: 200
     *         - message: "User role updated successfully"
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<ReqRes> updateUserRole(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String role, @RequestBody OurUsers newRole) {
        if (!"ADMIN".equals(role)) {
            ReqRes resp = new ReqRes();
            resp.setStatusCode(401);
            resp.setMessage("User is not authorized to perform this action");
            return ResponseEntity.status(resp.getStatusCode()).body(resp);
        }
        System.out.println("newRole: " + newRole);
        ReqRes resp = ourUserDetailsService.updateUserRole(id, newRole);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

}
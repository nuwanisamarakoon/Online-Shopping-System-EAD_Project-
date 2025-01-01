package com.example.usermanagement.Service;

import com.example.usermanagement.Dto.ReqRes;

import com.example.usermanagement.Entity.OurUsers;
import com.example.usermanagement.Entity.UserProfile;
import com.example.usermanagement.Repository.OurUserRepo;
import com.example.usermanagement.Repository.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

@Service
public class OurUserDetailsService implements UserDetailsService {
    private final OurUserRepo ourUserRepo;
    private final UserProfileRepo userProfileRepo;
    private final AmazonS3Service amazonS3Service;

    @Autowired
    public OurUserDetailsService(OurUserRepo ourUserRepo, UserProfileRepo userProfileRepo,  AmazonS3Service amazonS3Service) {
        this.ourUserRepo = ourUserRepo;
        this.userProfileRepo = userProfileRepo;
        this.amazonS3Service = amazonS3Service;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ourUserRepo.findByEmail(username).orElseThrow();
    }

    public ReqRes getAllUserProfiles() {
        ReqRes resp = new ReqRes();
        try{
            List<UserProfile> userProfiles = userProfileRepo.findAll();
            resp.setStatusCode(200);
            resp.setMessage("User Profiles Found");
            resp.setData(userProfiles);
        }catch (Exception e){
            e.printStackTrace();
            resp.setStatusCode(500);
            resp.setError("An error occurred while retrieving the User Profiles");
        }
        return resp;

    }


    public ReqRes getUserProfileById(Integer id) {
        ReqRes resp = new ReqRes();
        try {
            // Retrieve the UserProfile by ID
            UserProfile userProfile = userProfileRepo.findById(id)
                    .orElse(null); // Return null if not found

            if (userProfile != null) {
                // UserProfile found
                resp.setStatusCode(200);
                resp.setMessage("User Profile Found");
                resp.setData(userProfile);
            } else {
                // UserProfile not found
                resp.setStatusCode(404);
                resp.setMessage("User Profile Not Found");
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions
            resp.setStatusCode(500);
            resp.setMessage("An error occurred while retrieving the User Profile");
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes createUserProfile(Integer id, UserProfile userProfile, MultipartFile profilePicture) {
        ReqRes resp = new ReqRes();
        try {
            OurUsers user = ourUserRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            userProfile.setUser(user);
            user.setUserProfile(userProfile);

            if (profilePicture != null) {
                String profilePictureUrl = amazonS3Service.uploadFile(profilePicture, id);
                userProfile.setProfilePicture(profilePictureUrl);
            }

            userProfileRepo.save(userProfile);
            resp.setStatusCode(200);
            resp.setMessage("User Profile Created Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(500);
            resp.setError("An error occurred while creating the User Profile");
        }
        return resp;
    }

    public ReqRes updateUserProfile(Integer id, UserProfile updatedProfile, MultipartFile profilePicture) {
        ReqRes resp = new ReqRes();

        try {
            OurUsers user = ourUserRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserProfile existingProfile = user.getUserProfile();

            if (existingProfile == null) {
                throw new RuntimeException("User Profile not found");
            }
            if (updatedProfile.getFirstName() != null) existingProfile.setFirstName(updatedProfile.getFirstName());
            if (updatedProfile.getLastName() != null) existingProfile.setLastName(updatedProfile.getLastName());
            if (updatedProfile.getEmail() != null) existingProfile.setEmail(updatedProfile.getEmail());
            if (updatedProfile.getPostalCode() != null) existingProfile.setPostalCode(updatedProfile.getPostalCode());
            if (updatedProfile.getContactNumber() != null) existingProfile.setContactNumber(updatedProfile.getContactNumber());
            if (updatedProfile.getHouseNumber() != null) existingProfile.setHouseNumber(updatedProfile.getHouseNumber());
            if (updatedProfile.getAddressLine1() != null) existingProfile.setAddressLine1(updatedProfile.getAddressLine1());
            if (updatedProfile.getAddressLine2() != null) existingProfile.setAddressLine2(updatedProfile.getAddressLine2());

            if (updatedProfile.getProfilePicture() != null) {
                // Delete the existing profile picture from S3
                if (existingProfile.getProfilePicture() != null) {
                    amazonS3Service.deleteFile(existingProfile.getProfilePicture());
                }
                existingProfile.setProfilePicture(updatedProfile.getProfilePicture());
            }

            if (profilePicture != null) {
                // Upload the new profile picture to S3 with user ID
                String profilePictureUrl = amazonS3Service.uploadFile(profilePicture, id);
                existingProfile.setProfilePicture(profilePictureUrl);
            }

            userProfileRepo.save(existingProfile);
            resp.setStatusCode(200);
            resp.setMessage("User Profile Updated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(500);
            resp.setError("An error occurred while updating the User Profile");
        }

        return resp;
    }

    public ReqRes deleteUserProfile(Integer id) {
        ReqRes resp = new ReqRes();
        try {
            // Check if the UserProfile exists
            Optional<UserProfile> userProfileOptional = userProfileRepo.findById(id);
            if (userProfileOptional.isPresent()) {
                UserProfile userProfile = userProfileOptional.get();

                // Unlink the user reference to avoid orphaned records
                if (userProfile.getUser() != null) {
                    OurUsers user = userProfile.getUser();
                    user.setUserProfile(null);
                    userProfile.setUser(null);
                }

                // Delete the UserProfile
                userProfileRepo.delete(userProfile);

                // Response
                resp.setStatusCode(200);
                resp.setMessage("User Profile Deleted Successfully");
            } else {
                // UserProfile not found
                resp.setStatusCode(404);
                resp.setMessage("User Profile Not Found");
            }
        } catch (Exception e) {
            // Handle unexpected errors
            e.printStackTrace();
            resp.setStatusCode(500);
//            resp.setMessage("An error occurred while deleting the User Profile");
            resp.setError("Error while deleting user profile");
        }
        return resp;
    }

    public ReqRes updateUserRole(Integer id, OurUsers updatedUser) {
        ReqRes resp = new ReqRes();
        if (!"ADMIN".equals(updatedUser.getRole()) && !"USER".equals(updatedUser.getRole())) {
            resp.setStatusCode(400);
            resp.setMessage("Invalid role. Role must be either 'ADMIN' or 'USER'");
            return resp;
        }
        try {
            OurUsers user = ourUserRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setRole(updatedUser.getRole());
            ourUserRepo.save(user);
            resp.setStatusCode(200);
            resp.setMessage("User role updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(500);
            resp.setError("An error occurred while updating the user role");
        }
        return resp;
    }




}

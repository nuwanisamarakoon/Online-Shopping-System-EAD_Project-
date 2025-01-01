package com.example.usermanagement.Service;

import com.example.usermanagement.Dto.ReqRes;
import com.example.usermanagement.Entity.OurUsers;
import com.example.usermanagement.Entity.UserProfile;
import com.example.usermanagement.Repository.OurUserRepo;
import com.example.usermanagement.Repository.UserProfileRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Disabled
public class OurUserDetailsServiceTest {

    @Mock
    private OurUserRepo ourUserRepo;

    @Mock
    private UserProfileRepo userProfileRepo;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private OurUserDetailsService ourUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername() {
        OurUsers user = new OurUsers();
        user.setEmail("test@example.com");

        when(ourUserRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertEquals(user, ourUserDetailsService.loadUserByUsername("test@example.com"));
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(ourUserRepo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> ourUserDetailsService.loadUserByUsername("unknown@example.com"));

    }

    @Test
    void getAllUsersProfiles() {
        List<UserProfile> profiles = new ArrayList<>();
        profiles.add(new UserProfile());

        when(userProfileRepo.findAll()).thenReturn(profiles);

        assertEquals(profiles, ourUserDetailsService.getAllUserProfiles());
    }

    @Test
    void getUserProfileById() {
        UserProfile profile = new UserProfile();
        profile.setId(1);

        when(userProfileRepo.findById(1)).thenReturn(Optional.of(profile));

        ReqRes response = ourUserDetailsService.getUserProfileById(1);

        assertEquals(200, response.getStatusCode());
        assertEquals("User Profile Found", response.getMessage());
        assertEquals(profile, response.getUserProfile());

    }

    @Test
    void getUserProfileByIdNotFound() {
        when(userProfileRepo.findById(1)).thenReturn(Optional.empty());

        ReqRes response = ourUserDetailsService.getUserProfileById(1);

        assertEquals(404, response.getStatusCode());
        assertEquals("User Profile Not Found", response.getMessage());
    }

    @Test
    void createUserProfile() {
        UserProfile profile = new UserProfile();
        profile.setId(1);

        when(userProfileRepo.save(profile)).thenReturn(profile);

        ReqRes response = ourUserDetailsService.createUserProfile(1, profile, multipartFile);

        assertEquals(200, response.getStatusCode());
        assertEquals("User Profile Saved Successfully", response.getMessage());
        assertEquals(profile, response.getUserProfile());
    }

    @Test
    void updateUserProfile() {
        UserProfile profile = new UserProfile();
        profile.setId(1);
        UserProfile updatedProfile = new UserProfile();
        updatedProfile.setPostalCode("12345");

        when(userProfileRepo.findById(1)).thenReturn(Optional.of(profile));
        when(userProfileRepo.save(profile)).thenReturn(profile);

        ReqRes response = ourUserDetailsService.updateUserProfile(1, updatedProfile, multipartFile);

        assertEquals(200, response.getStatusCode());
        assertEquals("User Profile Updated Successfully", response.getMessage());
        assertEquals(updatedProfile, response.getUserProfile());
        assertEquals("12345", updatedProfile.getPostalCode());
    }

    @Test
    void updateUserProfileNotFound() {
        UserProfile profile = new UserProfile();
        profile.setPostalCode("12345");

        when(userProfileRepo.findById(1)).thenReturn(Optional.empty());

        ReqRes response = ourUserDetailsService.updateUserProfile(1, profile, multipartFile);

        assertEquals(404, response.getStatusCode());
        assertEquals("User Profile Not Found", response.getMessage());
    }

    @Test
    void deleteUserProfile() {
        when(userProfileRepo.existsById(1)).thenReturn(true);

        ReqRes response = ourUserDetailsService.deleteUserProfile(1);

        verify(userProfileRepo, times(1)).deleteById(1);
        assertEquals(200, response.getStatusCode());
        assertEquals("User Profile Deleted Successfully", response.getMessage());
    }

    @Test
    void deleteUserProfileNotFound() {
        when(userProfileRepo.existsById(1)).thenReturn(false);

        ReqRes response = ourUserDetailsService.deleteUserProfile(1);

        assertEquals(404, response.getStatusCode());
        assertEquals("User Profile Not Found", response.getMessage());
    }
}

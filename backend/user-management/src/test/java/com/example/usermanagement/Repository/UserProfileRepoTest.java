package com.example.usermanagement.Repository;

import com.example.usermanagement.Entity.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Disabled
public class UserProfileRepoTest {

    @Autowired
    private UserProfileRepo userProfileRepo;

    private UserProfile testUserProfile;

    @BeforeEach
    public void setUp() {

        testUserProfile = new UserProfile();
        testUserProfile.setFirstName("Test");
        testUserProfile.setLastName("User");
        testUserProfile.setEmail("testuser@example.com");
        testUserProfile.setHouseNumber("123");
        testUserProfile.setAddressLine1("Test St");
        testUserProfile.setAddressLine2("Test city");
        testUserProfile.setPostalCode("12345");


        userProfileRepo.save(testUserProfile);
    }

    @Test
    public void testFindById_ProfileExists() {

        Optional<UserProfile> foundProfile = userProfileRepo.findById(testUserProfile.getId());

        assertTrue(foundProfile.isPresent(), "User profile should be found");
        assertEquals(testUserProfile.getFirstName(), foundProfile.get().getFirstName());
        assertEquals(testUserProfile.getEmail(), foundProfile.get().getEmail());
        assertEquals(testUserProfile.getHouseNumber(), foundProfile.get().getHouseNumber());
        assertEquals(testUserProfile.getAddressLine1(), foundProfile.get().getAddressLine1());
        assertEquals(testUserProfile.getAddressLine2(), foundProfile.get().getAddressLine2());
        assertEquals(testUserProfile.getPostalCode(), foundProfile.get().getPostalCode());
    }

    @Test
    public void testSave_Profile() {

        UserProfile newProfile = new UserProfile();
        newProfile.setFirstName("New User");
        newProfile.setEmail("newuser@example.com");
        newProfile.setAddressLine1("456 New St");

        UserProfile savedProfile = userProfileRepo.save(newProfile);

        assertEquals("New User", savedProfile.getFirstName());
        assertEquals("newuser@example.com", savedProfile.getEmail());
        assertEquals("456 New St", savedProfile.getAddressLine1());
    }

    @Test
    public void testDelete_Profile() {

        userProfileRepo.deleteById(testUserProfile.getId());

        Optional<UserProfile> deletedProfile = userProfileRepo.findById(testUserProfile.getId());
        assertFalse(deletedProfile.isPresent(), "User profile should be deleted");
    }
}

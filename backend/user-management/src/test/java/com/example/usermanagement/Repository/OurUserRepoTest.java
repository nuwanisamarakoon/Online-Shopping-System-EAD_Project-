package com.example.usermanagement.Repository;

import com.example.usermanagement.Entity.OurUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Disabled
public class OurUserRepoTest {
    @Autowired
    private OurUserRepo ourUserRepo;

    private OurUsers testUser;

    @BeforeEach
    public void setUp() {
        testUser = new OurUsers();
        testUser.setEmail("test@example.com");
        testUser.setPassword("123@abc");
        testUser.setRole("USER");

        ourUserRepo.save(testUser);
    }

    @Test
    public void testFindByEmail_UserExists() {
        Optional<OurUsers> foundUser = ourUserRepo.findByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
        assertEquals(testUser.getPassword(), foundUser.get().getPassword());
        assertEquals(testUser.getRole(), foundUser.get().getRole());
    }

    @Test
    public void testFindByEmail_UserDoesNotExist() {
        Optional<OurUsers> foundUser = ourUserRepo.findByEmail("nonexistent@example.com");
        assertTrue(foundUser.isEmpty());
    }
}



package com.example.usermanagement.Repository;

import com.example.usermanagement.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Integer> {
}
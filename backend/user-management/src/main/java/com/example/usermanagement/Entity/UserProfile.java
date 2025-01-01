package com.example.usermanagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String postalCode;

    @Column(nullable = true)
    private String contactNumber;

    @Column(nullable = true)
    private String houseNumber;

    @Column(nullable = true)
    private String addressLine1;

    @Column(nullable = true)
    private String addressLine2;

    @Column(nullable = true)
    private String profilePicture;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private OurUsers user;

}

package com.vn.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@Table(name = "USERS")
public class Users {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "ADDRESS",columnDefinition = "NVARCHAR(100)")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @NotBlank
    private String address;

    @Column(name = "DATE_OF_BIRTH")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past")
    @NotNull
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @NotBlank
    @Column(name = "EMAIL", unique = true)
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Column(name = "FULL_NAME",columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;


    @NotBlank
    @Column(name = "IDENTITY_CARD", unique = true)
    private String identityCard;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "PASSWORD")
    private String password;

    // @Transient
    // private int numberOfInjections;

    @Transient
    private String rePassword;

    @NotBlank
    @Column(name = "PHONE", unique = true)
    private String phone;

    @Column(name = "POSITION" ,columnDefinition = "NVARCHAR(100)")
    private String position;

    @Column(name = "USERNAME", unique = true, nullable = false )
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    private String username;

    // @Column(name = "WORKING_PLACE",columnDefinition = "NVARCHAR(100)")
    // private String workingPlace;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

}

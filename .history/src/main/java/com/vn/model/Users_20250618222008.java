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
    @Column(name = "MaNhanVien")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maNhanVien;

    @Column(name = "MaChucVu", unique = true)
    private Integer maChucVu;

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

    @Column(name = "HoTen",columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String hoTen;

    @NotBlank
    @Column(name = "IDENTITY_CARD", unique = true)
    private String CMND;

    @Column(name = "GioiTinh")
    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Column(name = "Image")
    private String image;

    @Column(name = "PASSWORD")
    private String password;

    @Transient
    private String rePassword;

    @NotBlank
    @Column(name = "SoDienThoai", unique = true)
    private String soDienThoai;


    @Column(name = "USERNAME", unique = true, nullable = false )
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    private String username;


    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;

}

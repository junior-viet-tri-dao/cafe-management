package com.vn.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Getter
@Setter
@ToString
@Table(name = "Users")
public class Users {

    @Id
    @Column(name = "MaNhanVien")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maNhanVien;

    @ManyToOne
    @JoinColumn(name = "MaChucVu", referencedColumnName = "MaChucVu", nullable = true)
    private ChucVu chucVu;

    @Column(name = "DiaChi",columnDefinition = "nvarchar(50)")
    @Size(max = 50, message = "Address cannot exceed 50 characters")
    @NotBlank
    private String diaChi;

    @NotBlank
    @Column(name = "EMAIL", unique = true,columnDefinition = "nvarchar(50)")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String email;

    @Column(name = "HoTen",columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 50, message = "Full name cannot exceed 50 characters")
    private String hoTen;

    @NotBlank
    @Column(name = "CMND", unique = true)
    private String cMND;

    @Column(name = "GioiTinh")
    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Column(name = "Image")
    private String image;

    @Column(name = "PASSWORD")
    private String password;

    @NotBlank
    @Column(name = "SoDienThoai", unique = true)
    private String soDienThoai;


    @Column(name = "UserName", unique = true, nullable = false )
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    private String username;


    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "IsDeleted", nullable = false)
    @ColumnDefault("0")            
    private boolean isDeleted = false;


}

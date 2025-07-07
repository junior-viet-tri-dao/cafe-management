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
    @Size(max = 50, message = "Địa chỉ không được vượt quá 50 ký tự")
    @NotBlank(message = "Địa chỉ không được để trống")
    private String diaChi;

    @NotBlank(message = "Email không được để trống")
    @Column(name = "EMAIL", unique = true,columnDefinition = "nvarchar(50)")
    @Email(message = "Email không hợp lệ")
    @Size(max = 50, message = "Email không được vượt quá 50 ký tự")
    private String email;

    @Column(name = "HoTen",columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 50, message = "Họ tên không được vượt quá 50 ký tự")
    private String hoTen;

    @NotBlank(message = "CMND không được để trống")
    @Column(name = "CMND", unique = true,columnDefinition = "nvarchar(50)")
    private String cMND;

    @Column(name = "GioiTinh")
    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Column(name = "Image")
    private String image;

    @Column(name = "PASSWORD")
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Column(name = "SoDienThoai", unique = true)
    private String soDienThoai;


    @Column(name = "UserName", unique = true, nullable = false )
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 50, message = "Tên đăng nhập không được vượt quá 50 ký tự")
    private String username;


    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "IsDeleted", nullable = false)
    @ColumnDefault("0")            
    private boolean isDeleted = false;


}

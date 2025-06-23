package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTaiKhoan")
    private Integer maTaiKhoan;

    @Column(name = "TenDangNhap")
    private String tenDangNhap;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "QuyenHan")
    private String quyenHan;

    @Column(name = "Anh")
    private String anh;

    @OneToOne(mappedBy = "taiKhoan", cascade = CascadeType.ALL)
    private NhanVien nhanVien;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.quyenHan == null || this.quyenHan.isBlank()) {
            return List.of(new SimpleGrantedAuthority("EMPLOYEE"));
        }
        return List.of(new SimpleGrantedAuthority(this.quyenHan));
    }

    @Override
    public String getPassword() {
        return matKhau;
    }

    @Override
    public String getUsername() {
        return tenDangNhap;
    }
}

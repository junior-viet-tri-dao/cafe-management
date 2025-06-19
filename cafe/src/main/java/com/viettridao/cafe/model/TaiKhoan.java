package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "TaiKhoan")
@Data
public class TaiKhoan {
    @Id
    @Column(name = "MaTaiKhoan", nullable = false)
    private Integer id; // Tên field phải là 'id' hoặc tương ứng với getter/setter

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "TenDangNhap", nullable = false, length = 50)
    private String tenDangNhap;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "MatKhau", nullable = false)
    private String matKhau;

    @Size(max = 255)
    @Column(name = "QuyenHan")
    private String quyenHan;

    @Size(max = 255)
    @Column(name = "Anh")
    private String anh;

    @OneToMany(mappedBy = "maTaiKhoan")
    private Set<ChiTieu> chiTieus = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maTaiKhoan")
    private Set<NhanVien> nhanViens = new LinkedHashSet<>();

}
package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "NhanVien", schema = "dbo")
public class NhanVien {
    @Id
    @Column(name = "MaNhanVien", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaChucVu", nullable = false)
    private ChucVu maChucVu;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaTaiKhoan", nullable = false)
    private TaiKhoan maTaiKhoan;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Size(max = 15)
    @Nationalized
    @Column(name = "SoDienThoai", length = 15)
    private String soDienThoai;

    @Size(max = 200)
    @Nationalized
    @Column(name = "DiaChi", length = 200)
    private String diaChi;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "maNhanVien")
    private Set<ChiTietDatBan> chiTietDatBans = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maNhanVien")
    private Set<DonNhap> donNhaps = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maNhanVien")
    private Set<DonXuat> donXuats = new LinkedHashSet<>();

}
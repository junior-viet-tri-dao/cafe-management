package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "KhuyenMai", schema = "dbo")
public class KhuyenMai {
    @Id
    @Column(name = "MaKhuyenMai", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "TenKhuyenMai", nullable = false, length = 100)
    private String tenKhuyenMai;

    @NotNull
    @Column(name = "NgayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    @NotNull
    @Column(name = "NgayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "LoaiKhuyenMai", nullable = false, length = 50)
    private String loaiKhuyenMai;

    @NotNull
    @Column(name = "GiaTriGiam", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTriGiam;

    @NotNull
    @Column(name = "TrangThai", nullable = false)
    private Boolean trangThai = false;

    @Size(max = 255)
    @Nationalized
    @Column(name = "MoTa")
    private String moTa;

    @OneToMany(mappedBy = "maKhuyenMai")
    private Set<HoaDon> hoaDons = new LinkedHashSet<>();

}
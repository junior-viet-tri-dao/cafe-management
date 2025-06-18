package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ThucDon", schema = "dbo")
public class ThucDon {
    @Id
    @Column(name = "MaThucDon", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "TenMon", nullable = false, length = 100)
    private String tenMon;

    @NotNull
    @Column(name = "GiaTienHienTai", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTienHienTai;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "LoaiMon", nullable = false, length = 50)
    private String loaiMon;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "IsDeleted", nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "maThucDon")
    private Set<ChiTietHoaDon> chiTietHoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maThucDon")
    private Set<ChiTietThucDon> chiTietThucDons = new LinkedHashSet<>();

}
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
@Table(name = "ThietBi", schema = "dbo")
public class ThietBi {
    @Id
    @Column(name = "MaThietBi", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "TenThietBi", nullable = false, length = 100)
    private String tenThietBi;

    @NotNull
    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Size(max = 255)
    @Nationalized
    @Column(name = "GhiChu")
    private String ghiChu;

    @NotNull
    @Column(name = "NgayMua", nullable = false)
    private LocalDate ngayMua;

    @NotNull
    @Column(name = "DonGiaMua", nullable = false, precision = 18, scale = 2)
    private BigDecimal donGiaMua;

    @OneToMany(mappedBy = "maThietBi")
    private Set<DonNhap> donNhaps = new LinkedHashSet<>();

}
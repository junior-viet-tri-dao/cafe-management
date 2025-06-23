package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ThietBi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThietBi")
    private Integer maThietBi;

    @Column(name = "TenThietBi")
    private String tenThietBi;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "GhiChu")
    private String ghiChu;

    @Column(name = "NgayMua")
    private LocalDate ngayMua;

    @Column(name = "donGiaMua")
    private Double DonGiaMua;

    @Column(name = "isDeleted")
    private Boolean isDeleted;
}

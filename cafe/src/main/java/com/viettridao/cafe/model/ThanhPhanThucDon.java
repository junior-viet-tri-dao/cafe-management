package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ThanhPhanThucDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maThanhPhan;

    private String tenThanhPhan;

    private Double khoiLuong;

    @ManyToOne
    @JoinColumn(name = "MaDonVi")
    private DonViTinh donViTinh;

    @ManyToOne
    @JoinColumn(name = "MaThucDon")
    private ThucDon thucDon;
}

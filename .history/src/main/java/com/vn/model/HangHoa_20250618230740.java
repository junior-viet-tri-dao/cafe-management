package com.vn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "HangHoa")
public class HangHoa {
    @Id
    @Column(name = "MaHangHoa")
    private Integer maHangHoa;

    @Column(name = "TenHangHoa", nullable = false, length = 100)
    private String tenHangHoa;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @ManyToOne
    @JoinColumn(name = "MaDonViTinh", referencedColumnName = "MaDonViTinh", nullable = false)
    private DonViTinh donViTinh;

    @Column(name = "DonGia", nullable = false)
    private Double donGia;
} 
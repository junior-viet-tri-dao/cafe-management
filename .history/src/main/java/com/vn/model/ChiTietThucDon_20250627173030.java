package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ChiTietThucDon")
@IdClass(ChiTietThucDon.ChiTietThucDonId.class)
public class ChiTietThucDon {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaHangHoa", referencedColumnName = "MaHangHoa", nullable = false)
    private HangHoa hangHoa;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaThucDon", referencedColumnName = "MaThucDon", nullable = false)
    private ThucDon thucDon;

    @Column(name = "KhoiLuong", nullable = false)
    private Double khoiLuong;

    @Column(name = "DonViTinh", length = 50,columnDefinition = "nvarchar(50)")
    private String donViTinh;

    @Data
    public static class ChiTietThucDonId implements Serializable {
        private Integer hangHoa;
        private Integer thucDon;
    }
} 
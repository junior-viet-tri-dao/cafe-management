package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @Column(name = "MaHoaDon")
    private Integer maHoaDon;

    @Column(name = "TongTien", nullable = false)
    private Double tongTien;

    @Column(name = "NgayGioTao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayGioTao;

    @Column(name = "TrangThai", nullable = false)
    private Boolean trangThai;

    @ManyToOne
    @JoinColumn(name = "MaKhuyenMai", referencedColumnName = "MaKhuyenMai")
    private KhuyenMai khuyenMai;
} 
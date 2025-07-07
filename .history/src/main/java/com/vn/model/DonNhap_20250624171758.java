package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "DonNhap")
public class DonNhap {
    @Id
    @Column(name = "MaDonNhap", length = 10)
    private String maDonNhap;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien", nullable = false)
    private Users nhanVien;

    @ManyToOne
    @JoinColumn(name = "MaThietBi", referencedColumnName = "MaThietBi", nullable = false)
    private ThietBi thietBi;

    @ManyToOne
    @JoinColumn(name = "MaHangHoa", referencedColumnName = "MaHangHoa", nullable = false)
    private HangHoa hangHoa;

    @Column(name = "NgayNhap", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayNhap;

    @Column(name = "TongTien", nullable = false)
    private Double tongTien;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;
    
} 
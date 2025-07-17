package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "DonXuat")
public class DonXuat {
    @Id
    @Column(name = "MaDonXuat")
    private Integer maDonXuat;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien", nullable = false)
    private Users nhanVien;

    @ManyToOne
    @JoinColumn(name = "MaHangHoa", referencedColumnName = "MaHangHoa", nullable = false)
    private HangHoa hangHoa;


    @Column(name = "NgayXuat", nullable = false)
    @Temporal(TemporalType.DATE)
    
    private Date ngayXuat;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;
} 

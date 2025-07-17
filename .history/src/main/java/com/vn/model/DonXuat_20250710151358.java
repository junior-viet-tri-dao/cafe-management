package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotNull(message = "Ngày xuất không được để trống")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date ngayXuat;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;
} 

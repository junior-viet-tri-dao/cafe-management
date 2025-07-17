package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

import jakarta.validation.constraints.Min;
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
    @NotNull(message = "Mã đơn xuất không được để trống")
    private Integer maDonXuat;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien", nullable = false)
    private Users nhanVien;

    @ManyToOne
    @JoinColumn(name = "MaHangHoa", referencedColumnName = "MaHangHoa", nullable = false)
    @ToString.Exclude
    private HangHoa hangHoa;

    @Column(name = "NgayXuat", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Ngày xuất không được để trống")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date ngayXuat;

    @Column(name = "SoLuong", nullable = false)
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer soLuong;
} 

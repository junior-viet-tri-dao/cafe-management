package com.vn.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
@ToString
@Table(name = "DonNhap")
public class DonNhap {
    @Id
    @Column(name = "MaDonNhap", length = 50)
    private String maDonNhap;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien")
    private Users nhanVien;

    @ManyToOne
    @JoinColumn(name = "MaHangHoa", referencedColumnName = "MaHangHoa")
    private HangHoa hangHoa;

    @Column(name = "NgayNhap", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Ngày nhập không được để trống")
    @PastOrPresent(message = "Ngày nhập phải là ngày hiện tại hoặc trong quá khứ")
    private Date ngayNhap;

    @Column(name = "TongTien", nullable = false)
    @NotNull(message = "Tổng tiền không được để trống")
    @ColumnDefault("0.0")
    @DecimalMin(value = "0.01", message = "Tổng tiền phải lớn hơn 0")
    private Double tongTien;

    @Column(name = "SoLuong", nullable = false)
    @NotNull(message = "Số lượng không được để trống")
    @ColumnDefault("0")
    @DecimalMin(value = "1", message = "Số lượng phải lớn hơn 0")
    private Integer soLuong;
    
} 

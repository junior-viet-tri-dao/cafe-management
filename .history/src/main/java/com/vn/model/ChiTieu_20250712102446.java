package com.vn.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ChiTieu")
public class ChiTieu {
    @Id
    @NotNull(message = "Mã chi tiêu không được để trống")
    @Column(name = "MaChiTieu")
    private Integer maChiTieu;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien", nullable = false)
    private Users nhanVien;

    @Column(name = "SoTien", nullable = false)
    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.01", message = "Số tiền phải lớn hơn 0")
    private Double soTien;

    @Column(name = "TenKhoanChi", length = 100,columnDefinition = "nvarchar(100)")
    @NotBlank(message = "Tên khoản chi không được để trống")
    @Size(max = 100, message = "Tên khoản chi tối đa 100 ký tự")
    private String tenKhoanChi;

    @Column(name = "NgayChi", nullable = false)
    @NotNull(message = "Ngày chi không được để trống")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date ngayChi;
}
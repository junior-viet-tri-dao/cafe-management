package com.vn.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "HangHoa")
public class HangHoa {
    @Id
    @Column(name = "MaHangHoa")
    @NotNull(message = "Mã Hàng Hóa Không được để trống")
    private Integer maHangHoa;

    @Column(name = "TenHangHoa", nullable = false, length = 100,columnDefinition = "nvarchar(50)")
    @NotBlank(message = "Tên Hàng Hóa Không được để trống")
    private String tenHangHoa;

    @Column(name = "SoLuong", nullable = false)
    @NotNull(message = "Số Lượng Không được để trống")
    private Integer soLuong;

    @ManyToOne
    @JoinColumn(name = "MaDonViTinh", referencedColumnName = "MaDonViTinh", nullable = false)
    private DonViTinh donViTinh;

    @Column(name = "DonGia", nullable = false)
    @NotNull(message = "Đơn Giá Không được để trống")
    private Double donGia;

    @OneToMany(mappedBy = "hangHoa", cascade = CascadeType.ALL)
    private List<DonNhap> donNhaps;

    
} 
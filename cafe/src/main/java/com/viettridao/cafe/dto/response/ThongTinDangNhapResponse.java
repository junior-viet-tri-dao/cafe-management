package com.viettridao.cafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ThongTinDangNhapResponse {
    private String hoTen;
    private String chucVu;
    private String diaChi;
    private String soDienThoai;
    private BigDecimal luong;
    private String tenDangNhap;
    private String matKhau;
}

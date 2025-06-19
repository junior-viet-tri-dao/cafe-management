package com.viettridao.cafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String hoTen;
    private String chucVu;
    private String diaChi;
    private String soDienThoai;
    private BigDecimal luong;
    private String tenDangNhap;
    private String matKhau;
}

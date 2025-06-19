package com.viettridao.cafe.dto.request;

import lombok.Data;

@Data
public class UpdateNhanVienRequest {
    private Integer maNhanVien;
    private String hoTen;
    private String diaChi;
    private Integer maChucVu;
    private String soDienThoai;

    // Thông tin tài khoản
    private String tenDangNhap;
    private String matKhauMoi;
}

package com.viettridao.cafe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddNhanVienRequest {
    private String hoTen;
    private Integer maChucVu;
    private Double luong;
    private String diaChi;
    private String soDienThoai;
    private String tenDangNhap;
    private String matKhau;
}

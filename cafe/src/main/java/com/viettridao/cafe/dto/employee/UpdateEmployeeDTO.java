package com.viettridao.cafe.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeDTO {
    private Integer maNhanVien;

    private String hoTen;

    private Integer maChucVu;

    private String diaChi;

    private String sdt;

    private Double luong;

    private String tenDangNhap;

    private String matKhau;

    private String anh;
}

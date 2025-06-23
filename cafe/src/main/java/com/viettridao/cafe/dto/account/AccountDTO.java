package com.viettridao.cafe.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String hoTen;

    private String chucVu;

    private String diaChi;

    private String sdt;

    private Double luong;

    private String tenDangNhap;

    private String matKhau;

    private String anh;
}

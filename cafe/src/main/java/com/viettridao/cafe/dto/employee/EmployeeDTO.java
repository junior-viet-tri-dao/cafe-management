package com.viettridao.cafe.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Integer maNhanVien;

    private String hoTen;

    private String chucVu;

    private Double luong;
}

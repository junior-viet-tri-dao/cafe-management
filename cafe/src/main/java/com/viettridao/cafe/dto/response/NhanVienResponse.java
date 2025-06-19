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
public class NhanVienResponse {
    private Integer maNhanVien;
    private String hoTen;
    private String chucVu;
    private BigDecimal luong;
}

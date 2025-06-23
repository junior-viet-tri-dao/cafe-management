package com.viettridao.cafe.dto.commodity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommodityDTO {
    private Integer maDonNhap;

    private String tenHangHoa;

    private LocalDate ngayNhap;

    private LocalDate ngayXuat;

    private Integer soLuongNhap;

    private Integer soLuongXuat;

    private String tenDonVi;

    private Double donGia;
}

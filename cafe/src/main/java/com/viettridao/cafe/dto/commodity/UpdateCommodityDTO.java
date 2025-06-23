package com.viettridao.cafe.dto.commodity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommodityDTO {
    private Integer maDonNhap;

    private Integer maHangHoa;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate ngayNhap;

    private Integer soLuongNhap;

    private Double donGia;

    private String tenDonVi;
}

package com.viettridao.cafe.dto.commodity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
public class CreateCommodityDTO {
    private Integer maHangHoa;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate ngayNhap;

    private Integer soLuongNhap;

    private Double donGia;
}

package com.viettridao.cafe.dto.marketing;

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
public class MarketingDTO {
    private Integer maKhuyenMai;

    private String tenKhuyenMai;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate ngayBatDau;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate ngayKetThuc;

    private Double giaTriGiam;
}

package com.viettridao.cafe.dto.marketing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMarketingDTO {
    private String tenKhuyenMai;

    private LocalDate ngayBatDau;

    private LocalDate ngayKetThuc;

    private Double giaTriGiam;
}

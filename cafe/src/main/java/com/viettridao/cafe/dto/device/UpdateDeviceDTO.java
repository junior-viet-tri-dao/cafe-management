package com.viettridao.cafe.dto.device;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateDeviceDTO {
    private Integer maThietBi;

    private String tenThietBi;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate ngayMua;

    private Integer soLuong;

    private Double donGiaMua;
}

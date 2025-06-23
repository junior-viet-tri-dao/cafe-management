package com.viettridao.cafe.dto.device;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeviceDTO {
    private String tenThietBi;

    private LocalDate ngayMua;

    private Integer soLuong;

    private Double donGiaMua;
}

package com.viettridao.cafe.dto.request.equipment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateEquipmentRequest {
    private Integer id;

    private String equipmentName;

    private Integer quantity;

    private LocalDate purchaseDate;

    private Double purchasePrice;

}

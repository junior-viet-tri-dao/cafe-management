package com.viettridao.cafe.dto.request.equipment;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentCreateRequest {

    private String equipmentName;

    private String notes;

    private LocalDate purchaseDate;

    private Double purchasePrice;

    private Integer quantity;

}

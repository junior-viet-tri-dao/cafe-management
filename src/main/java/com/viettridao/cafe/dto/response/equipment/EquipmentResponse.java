package com.viettridao.cafe.dto.response.equipment;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EquipmentResponse {
    private Integer id;
    private String equipmentName;
    private Integer quantity;
    private String notes;
    private LocalDate purchaseDate;
    private Double purchasePrice;
    private Boolean isDeleted;
}

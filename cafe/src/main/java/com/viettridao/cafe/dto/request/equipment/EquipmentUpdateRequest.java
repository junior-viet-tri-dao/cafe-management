package com.viettridao.cafe.dto.request.equipment;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class EquipmentUpdateRequest {

    private Integer id;

    private String equipmentName;

    private String notes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    private Double purchasePrice;

    private Integer quantity;

}

package com.viettridao.cafe.dto.response.equipment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EquipmentResponse {
    private Integer id;

    private String equipmentName;

    private Integer quantity;

    private String notes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    private Double purchasePrice;

    private Boolean isDeleted;


}

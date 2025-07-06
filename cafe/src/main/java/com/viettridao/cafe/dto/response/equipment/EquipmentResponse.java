package com.viettridao.cafe.dto.response.equipment;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import com.viettridao.cafe.dto.response.imports.ImportResponse;

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

    private List<ImportResponse> imports;

}

package com.viettridao.cafe.dto.response.equipment;

import com.viettridao.cafe.dto.response.ImportResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EquipmentResponse {

    private Integer id;

    private String equipmentName;

    private Integer quantity;

    private String notes;

    private LocalDate purchaseDate;

    private Double purchasePrice;

    private Boolean isDeleted;

    private List<ImportResponse> imports;
}

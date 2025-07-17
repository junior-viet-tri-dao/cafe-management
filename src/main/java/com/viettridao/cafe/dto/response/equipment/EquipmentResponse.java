package com.viettridao.cafe.dto.response.equipment;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentResponse {
    private Integer id;
    private String equipmentName;
    private Integer quantity;
    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    private BigDecimal totalAmount;
    private Boolean isDeleted;
}

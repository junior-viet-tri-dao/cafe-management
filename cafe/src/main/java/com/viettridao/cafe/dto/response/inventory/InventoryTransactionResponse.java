package com.viettridao.cafe.dto.response.inventory;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryTransactionResponse {

    private Integer id;

    private String productName;

    private String unitName;

    private Integer quantity;

    private Double unitPrice;

    private Double totalAmount;

    private LocalDate importDate;

    private LocalDate exportDate;

    private String type;
}

package com.viettridao.cafe.dto;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EquipmentDTO {
	private Integer id;
	private String equipmentName;
	private LocalDate purchaseDate;
	private Integer quantity;
	private Double purchasePrice;
	private String notes;

	public Double getTotalPrice() {
		if (quantity != null && purchasePrice != null) {
			return quantity * purchasePrice;
		}
		return 0.0;
	}
}

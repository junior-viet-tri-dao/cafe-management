package com.viettridao.cafe.dto.response.product;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
	
	private Integer id;
	
	private String productName;
	
	private Integer quantity;
	
	private Double productPrice;
	
	private Integer unitId;
	
	private String unitName;
	
	private LocalDate latestImportDate;
	
	private LocalDate latestExportDate;
	
	private Double totalAmount;
}

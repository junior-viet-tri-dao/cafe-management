package com.viettridao.cafe.dto.response.imports;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.dto.response.product.ProductResponse;

@Getter
@Setter
public class ImportResponse {

    private Integer id;

    private LocalDate importDate;

    private Double totalAmount;

    private Integer quantity;

    private Boolean deleted;

    private EmployeeResponse employee;

    private EquipmentResponse equipment;

    private ProductResponse product;

}

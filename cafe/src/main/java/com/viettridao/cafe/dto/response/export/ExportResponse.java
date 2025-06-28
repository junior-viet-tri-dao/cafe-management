package com.viettridao.cafe.dto.response.export;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.dto.response.product.ProductResponse;

@Getter
@Setter
public class ExportResponse {

    private Integer id;

    private Double totalExportAmount;

    private LocalDate exportDate;

    private Integer quantity;

    private Boolean deleted;

    private EmployeeResponse employee;

    private ProductResponse product;

}

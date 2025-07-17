package com.viettridao.cafe.dto.response.import_response;

import java.time.LocalDate;

import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ProductEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportResponse {
    private Integer id;
    private LocalDate importDate;
    private Double totalAmount;
    private Integer quantity;
    private Boolean isDeleted;
    private EmployeeEntity employee;
    private ProductEntity product;
}

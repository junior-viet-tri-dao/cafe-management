package com.viettridao.cafe.dto.request.imports;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportCreateRequest {

    private LocalDate importDate;

    private Double totalAmount;

    private Integer quantity;

    private Integer employeeId;

    private Integer productId;

}

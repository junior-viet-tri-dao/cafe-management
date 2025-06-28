package com.viettridao.cafe.dto.request.export;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExportUpdateRequest {

    private Integer id;

    private LocalDate exportDate;

    private Double totalAmount;

    private Integer quantity;

    private Integer employeeId;

    private Integer productId;

}

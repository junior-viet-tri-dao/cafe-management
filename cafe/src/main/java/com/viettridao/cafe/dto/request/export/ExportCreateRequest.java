package com.viettridao.cafe.dto.request.export;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportCreateRequest {

    private Double totalExportAmount;

    private LocalDate exportDate;

    private Integer quantity;

    private Integer employeeId;

    private Integer productId;

}

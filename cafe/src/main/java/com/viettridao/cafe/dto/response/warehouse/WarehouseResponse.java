package com.viettridao.cafe.dto.response.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

/**
 * DTO cho việc biểu diễn dữ liệu liên quan đến warehouse.
 * Lớp này bao gồm chi tiết về imports, exports và thông tin sản phẩm.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse {
    private Integer importId;
    private Integer exportId;
    private String productName;
    private Date importDate;
    private Date exportDate;
    private Integer quantityImport;
    private Integer quantityExport;
    private String unitName;
    private Double productPrice;
}
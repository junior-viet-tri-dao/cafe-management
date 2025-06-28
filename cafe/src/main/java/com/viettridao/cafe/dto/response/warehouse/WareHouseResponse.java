package com.viettridao.cafe.dto.response.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WareHouseResponse {
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

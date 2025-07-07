package com.viettridao.cafe.dto.response.warehouse;

import com.viettridao.cafe.dto.response.PageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO for paginated response of warehouse data.
 * Extends the generic PageResponse class.
 */
@Setter
@Getter
public class WarehousePageResponse extends PageResponse {
    private List<WarehouseResponse> warehouses;

}

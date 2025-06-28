package com.viettridao.cafe.dto.response.warehouse;

import com.viettridao.cafe.dto.response.PageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WareHousePageResponse extends PageResponse {
    private List<WareHouseResponse> warehouses;
}

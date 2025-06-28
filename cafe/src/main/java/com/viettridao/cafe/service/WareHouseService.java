package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.response.warehouse.WareHousePageResponse;

public interface WareHouseService {
    WareHousePageResponse getAllWareHouses(String keyword, int page, int size);
}

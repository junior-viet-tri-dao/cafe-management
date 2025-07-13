package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.response.unit.UnitResponse;

public interface UnitService {
	List<UnitResponse> findAll();
}

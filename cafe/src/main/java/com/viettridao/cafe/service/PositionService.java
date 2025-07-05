package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.model.PositionEntity;

public interface PositionService {
	PositionEntity getPositionById(Integer id);

	List<PositionEntity> getPositions();
}

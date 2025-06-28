package com.viettridao.cafe.service;

import com.viettridao.cafe.model.PositionEntity;

import java.util.List;

public interface PositionService {
    PositionEntity getPositionById(Integer id);
    List<PositionEntity> getPositions();
}

package com.viettridao.cafe.service;

import com.viettridao.cafe.model.UnitEntity;

import java.util.List;

public interface UnitService {
    List<UnitEntity> getAllUnits();
    UnitEntity getUnitById(Integer id);
}

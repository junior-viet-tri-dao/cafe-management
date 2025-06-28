package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;

    @Override
    public List<UnitEntity> getAllUnits() {
        return unitRepository.findAllByIsDeleted(false);
    }

    @Override
    public UnitEntity getUnitById(Integer id) {
        return unitRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy đơn vị tính có id=" + id));
    }
}

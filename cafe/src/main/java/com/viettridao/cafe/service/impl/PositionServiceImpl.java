package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.PositionRepository;
import com.viettridao.cafe.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;

    @Override
    public PositionEntity getPositionById(Integer id) {
        return positionRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy chức vụ id=" + id));
    }

    @Override
    public List<PositionEntity> getPositions() {
        return positionRepository.getAllPositions();
    }
}

package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.PositionRepository;
import com.viettridao.cafe.service.PositionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
	private final PositionRepository positionRepository;

	@Override
	public PositionEntity getPositionById(Integer id) {
		return positionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy chức vụ id=" + id));
	}

	@Override
	public List<PositionEntity> getPositions() {
		return positionRepository.getAllPositions();
	}
}

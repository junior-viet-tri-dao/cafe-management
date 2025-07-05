package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.response.unit.UnitResponse;
import com.viettridao.cafe.mapper.UnitMapper;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.UnitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

	private final UnitRepository unitRepository;
	private final UnitMapper unitMapper;

	@Override
	public List<UnitResponse> getAll() {
		return unitMapper.toDtoList(unitRepository.findByIsDeletedFalse());
	}
}

package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.mapper.ImportMapper;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ImportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ImportServiceImpl implements ImportService {

	private final ImportRepository importRepository;
	private final ProductRepository productRepository;
	private final EmployeeRepository employeeRepository;
	private final ImportMapper importMapper;

	@Override
	public ImportResponse createImport(ImportRequest request) {
		ProductEntity product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
				.orElseThrow(() -> new RuntimeException("Hàng hóa không tồn tại"));

		EmployeeEntity employee = employeeRepository.findById(request.getEmployeeId())
				.orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

		ImportEntity entity = importMapper.fromRequest(request);
		entity.setProduct(product);
		entity.setEmployee(employee);
		entity.setIsDeleted(false);

		entity.setTotalAmount(request.getQuantity() * request.getProductPrice());

		product.setQuantity(product.getQuantity() + request.getQuantity());

		importRepository.save(entity);
		return importMapper.toDto(entity);
	}

	@Override
	public List<ImportResponse> getImportsByProduct(Integer productId) {
		List<ImportEntity> list = importRepository.findByProductIdAndIsDeletedFalse(productId);
		return importMapper.toDtoList(list);
	}

	@Override
	public List<ImportResponse> getAll() {
		return importMapper.toDtoList(importRepository.findByIsDeletedFalse());
	}
}

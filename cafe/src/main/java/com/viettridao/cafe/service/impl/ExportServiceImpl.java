package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;
import com.viettridao.cafe.mapper.ExportMapper;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ExportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ExportServiceImpl implements ExportService {

	private final ExportRepository exportRepository;
	private final ProductRepository productRepository;
	private final EmployeeRepository employeeRepository;
	private final ExportMapper exportMapper;

	@Override
	public ExportResponse createExport(ExportRequest request) {
		ProductEntity product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
				.orElseThrow(() -> new RuntimeException("Hàng hóa không tồn tại"));

		EmployeeEntity employee = employeeRepository.findById(request.getEmployeeId())
				.orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

		if (request.getQuantity() > product.getQuantity()) {
			throw new RuntimeException("Vượt quá số lượng tồn kho");
		}

		ExportEntity entity = exportMapper.fromRequest(request);
		entity.setProduct(product);
		entity.setEmployee(employee);
		entity.setIsDeleted(false);

		double totalExportAmount = request.getQuantity() * product.getProductPrice();
		entity.setTotalExportAmount(totalExportAmount);
		product.setQuantity(product.getQuantity() - request.getQuantity());

		exportRepository.save(entity);
		return exportMapper.toDto(entity);
	}

	@Override
	public List<ExportResponse> getExportsByProduct(Integer productId) {
		List<ExportEntity> list = exportRepository.findByProductIdAndIsDeletedFalse(productId);
		return exportMapper.toDtoList(list);
	}

	@Override
	public List<ExportResponse> getAll() {
		return exportMapper.toDtoList(exportRepository.findByIsDeletedFalse());
	}
}

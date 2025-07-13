package com.viettridao.cafe.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;
import com.viettridao.cafe.mapper.ExportMapper;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ExportService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

	private final ExportRepository exportRepository;
	private final ProductRepository productRepository;
	private final ImportRepository importRepository;
	private final ExportMapper exportMapper;

	@Override
	@Transactional
	public ExportResponse createExport(ExportRequest request) {
		exportProduct(request);

		Optional<ExportEntity> latestExport = exportRepository.findLatestByProductId(request.getProductId())
				.stream()
				.findFirst();

		if (latestExport.isEmpty()) {
			throw new RuntimeException("Không tìm thấy đơn xuất gần nhất");
		}

		return exportMapper.toDto(latestExport.get());
	}

	@Override
	@Transactional
	public void exportProduct(ExportRequest request) {
		ProductEntity product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		if (product.getQuantity() < request.getQuantity()) {
			throw new RuntimeException("Số lượng xuất vượt quá tồn kho");
		}

		ImportEntity latestImport = importRepository.findLatestByProductId(product.getId()).stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nhập gần nhất cho sản phẩm"));

		Double unitPrice = latestImport.getPrice();

		ExportEntity entity = exportMapper.fromRequest(request);
		entity.setIsDeleted(false);
		entity.setProduct(product);
		entity.setTotalExportAmount(request.getQuantity() * unitPrice);

		// Có thể cần gán thêm nhân viên nếu có từ session, request, v.v. nếu cần thiết
		// entity.setEmployee(...);

		exportRepository.save(entity);

		product.setQuantity(product.getQuantity() - request.getQuantity());
		productRepository.save(product);
	}

	@Override
	public List<ExportResponse> getAll() {
		return exportRepository.findByIsDeletedFalse().stream()
				.map(exportMapper::toDto)
				.toList();
	}

	@Override
	public List<ExportResponse> getExportsByProduct(Integer productId) {
		return exportRepository.findByProductIdAndIsDeletedFalse(productId).stream()
				.map(exportMapper::toDto)
				.toList();
	}

	@Override
	public Page<ExportResponse> getExportsByProductId(Integer productId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return exportRepository.findByProductIdAndIsDeletedFalse(productId, pageable)
				.map(exportMapper::toDto);
	}
}

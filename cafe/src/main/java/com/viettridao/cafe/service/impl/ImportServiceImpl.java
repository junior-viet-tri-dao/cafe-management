package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.mapper.ImportMapper;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.ImportService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

	private final ImportRepository importRepository;
	private final ProductRepository productRepository;
	private final UnitRepository unitRepository;
	private final ImportMapper importMapper;

	@Override
	@Transactional
	public ImportResponse createImport(ImportRequest request) {
		// ✅ Kiểm tra dữ liệu đầu vào
		if (request.getQuantity() == null || request.getQuantity() <= 0) {
			throw new IllegalArgumentException("Số lượng nhập phải lớn hơn 0.");
		}
		if (request.getPrice() == null || request.getPrice() < 0) {
			throw new IllegalArgumentException("Đơn giá nhập không được để trống hoặc âm.");
		}

		ProductEntity product;

		// ✅ Tìm hoặc tạo sản phẩm
		if (request.getProductId() != null) {
			product = productRepository.findById(request.getProductId()).orElseThrow(
					() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + request.getProductId()));
		} else {
			product = productRepository.findByProductNameIgnoreCase(request.getProductName()).orElseGet(() -> {
				UnitEntity unit = unitRepository.findByIdAndIsDeletedFalse(request.getUnitId()).orElseThrow(
						() -> new RuntimeException("Không tìm thấy đơn vị tính với ID: " + request.getUnitId()));

				ProductEntity newProduct = new ProductEntity();
				newProduct.setProductName(request.getProductName());
				newProduct.setUnit(unit);
				newProduct.setQuantity(0);
				newProduct.setIsDeleted(false);
				return productRepository.save(newProduct);
			});
		}

		// ✅ Tạo đơn nhập mới
		ImportEntity entity = importMapper.fromRequest(request);
		entity.setProduct(product);
		entity.setPrice(request.getPrice());
		entity.setTotalAmount(request.getQuantity() * request.getPrice());
		entity.setIsDeleted(false);
		importRepository.save(entity);

		// ✅ Cập nhật tồn kho
		int updatedQuantity = product.getQuantity() + request.getQuantity();
		product.setQuantity(updatedQuantity);
		productRepository.save(product);

		// ✅ Trả về bản ghi đơn nhập mới nhất
		return importMapper.toDto(importRepository.findLatestByProductId(product.getId()).get(0));
	}

	@Override
	public List<ImportResponse> getAll() {
		// ⚠️ Dùng truy vấn JOIN FETCH để tránh lỗi productName = null
		return importRepository.findAllWithProduct().stream().map(importMapper::toDto).toList();
	}

	@Override
	public List<ImportResponse> getImportsByProduct(Integer productId) {
		return importRepository.findByProductIdAndIsDeletedFalse(productId).stream().map(importMapper::toDto).toList();
	}

	@Override
	public Page<ImportResponse> getImportsByProductId(Integer productId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return importRepository.findByProductIdAndIsDeletedFalse(productId, pageable).map(importMapper::toDto);
	}
}

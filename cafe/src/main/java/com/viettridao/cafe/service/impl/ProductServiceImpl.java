package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final UnitRepository unitRepository;
	private final ProductMapper productMapper;

	@Override
	public Page<ProductResponse> getAllPaged(int page, int size) {
		return productRepository.findAllByIsDeletedFalse(PageRequest.of(page, size)).map(productMapper::toDto);
	}

	@Override
	public List<ProductResponse> getAll() {
		List<ProductEntity> products = productRepository.findAllByIsDeletedFalse();
		return productMapper.toDtoList(products);
	}

	@Override
	public ProductResponse getById(Integer id) {
		ProductEntity product = productRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
		return productMapper.toDto(product);
	}

	@Override
	@Transactional
	public void update(Integer id, ProductRequest request) {
		ProductEntity product = productRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		productMapper.updateEntityFromRequest(request, product);

		UnitEntity unit = unitRepository.findById(request.getUnitId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị tính"));

		product.setUnit(unit);

		productRepository.save(product);
	}

	@Override
	public List<ProductResponse> search(String keyword) {
		List<ProductEntity> products = productRepository
				.findByProductNameContainingIgnoreCaseAndIsDeletedFalse(keyword);
		return productMapper.toDtoList(products);
	}

	@Override
	public Page<ProductResponse> search(String keyword, int page, int size) {
		return productRepository
				.findByProductNameContainingIgnoreCaseAndIsDeletedFalse(keyword, PageRequest.of(page, size))
				.map(productMapper::toDto);
	}
}

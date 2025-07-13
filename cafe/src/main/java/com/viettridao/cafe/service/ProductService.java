package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;

public interface ProductService {

	List<ProductResponse> findAll();

	Page<ProductResponse> findAllPaged(int page, int size);

	ProductResponse findById(Integer id);

	void save(ProductRequest request);

	void update(Integer id, ProductRequest request);

	void delete(Integer id);

	int getCurrentStock(Integer productId);

	List<ProductResponse> search(String keyword);

	Page<ProductResponse> search(String keyword, int page, int size);
}

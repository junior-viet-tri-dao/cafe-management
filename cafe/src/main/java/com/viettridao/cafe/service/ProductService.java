package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;

public interface ProductService {

	Page<ProductResponse> getAllPaged(int page, int size);

	List<ProductResponse> getAll();

	ProductResponse getById(Integer id);

	void update(Integer id, ProductRequest request);

	Page<ProductResponse> search(String keyword, int page, int size);

	List<ProductResponse> search(String keyword);
}

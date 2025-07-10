package com.viettridao.cafe.service;

// Import các thư viện cần thiết
import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.dto.request.product.UpdateProductRequest;
import com.viettridao.cafe.dto.response.product.ProductPageResponse;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.model.ProductEntity;

import java.util.List;

/**
 * Service cho thực thể ProductEntity.
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến sản phẩm (Product).
 */
public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductPageResponse getAllProducts(String keyword, int page, int size);

    boolean existsByProductNameAndIsDeletedFalse(String productName);

    /**
     * Kiểm tra trùng tên sản phẩm, loại trừ chính nó (không tính sản phẩm đang cập
     * nhật)
     * 
     * @param productName tên sản phẩm
     * @param productId   id sản phẩm cần loại trừ
     * @return true nếu đã tồn tại tên sản phẩm khác với id này
     */
    boolean existsByProductNameAndIsDeletedFalseExceptId(String productName, Integer productId);

    ProductEntity createProduct(CreateProductRequest request);

    void deleteProduct(Integer id);

    void updateProduct(UpdateProductRequest request);

    ProductEntity getProductById(Integer id);
}

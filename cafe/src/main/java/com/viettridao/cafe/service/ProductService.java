package com.viettridao.cafe.service;

// Import các thư viện cần thiết
import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.model.ProductEntity;

import java.util.List;

/**
 * Service cho thực thể ProductEntity.
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến sản phẩm (Product).
 */
public interface ProductService {

    /**
     * Tạo mới một sản phẩm.
     *
     * @param request Đối tượng chứa thông tin cần thiết để tạo sản phẩm mới.
     * @return Thực thể ProductEntity vừa được tạo.
     */
    ProductEntity createProduct(CreateProductRequest request);

    /**
     * Lấy thông tin chi tiết của một sản phẩm dựa trên ID.
     *
     * @param id ID của sản phẩm cần lấy thông tin.
     * @return Thực thể ProductEntity tương ứng với ID.
     */
    ProductEntity getProductById(Integer id);

    /**
     * Lấy danh sách tất cả các sản phẩm.
     *
     * @return Danh sách các thực thể ProductEntity.
     */
    List<ProductEntity> getAllProducts();
}

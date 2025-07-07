package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lớp triển khai các phương thức xử lý logic liên quan đến hàng hóa.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    // Repository quản lý dữ liệu hàng hóa
    private final ProductRepository productRepository;

    // Service xử lý logic liên quan đến đơn vị tính
    private final UnitService unitService;

    /**
     * Tạo mới một hàng hóa.
     *
     * @param request Đối tượng chứa thông tin hàng hóa cần tạo.
     * @return Đối tượng ProductEntity đã được lưu vào cơ sở dữ liệu.
     */
    @Transactional
    @Override
    public ProductEntity createProduct(CreateProductRequest request) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(request.getProductName());
        productEntity.setQuantity(0);
        productEntity.setProductPrice(request.getProductPrice());

        UnitEntity unit = unitService.getUnitById(request.getUnitId());
        productEntity.setUnit(unit);

        return productRepository.save(productEntity);
    }

    /**
     * Lấy thông tin hàng hóa theo ID.
     *
     * @param id ID của hàng hóa cần tìm.
     * @return Đối tượng ProductEntity tương ứng với ID.
     * @throws RuntimeException nếu không tìm thấy hàng hóa với ID đã cho.
     */
    @Override
    public ProductEntity getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa có id=" + id));
    }

    /**
     * Lấy danh sách tất cả các hàng hóa chưa bị xóa.
     *
     * @return Danh sách các đối tượng ProductEntity.
     */
    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAllByIsDeleted(false);
    }
}

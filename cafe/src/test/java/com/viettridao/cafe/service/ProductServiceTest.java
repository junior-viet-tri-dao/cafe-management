package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UnitService unitService;

    @Autowired
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Tạo sản phẩm thành công")
    void testCreateProduct_Success() {
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("Cà phê sữa");
        request.setProductPrice(25000d);
        request.setUnitId(1);
        UnitEntity unit = new UnitEntity();
        unit.setId(1);
        unit.setUnitName("Ly");
        when(unitService.getUnitById(1)).thenReturn(unit);
        ProductEntity saved = new ProductEntity();
        saved.setId(1);
        saved.setProductName("Cà phê sữa");
        saved.setProductPrice(25000d);
        saved.setUnit(unit);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(saved);
        ProductEntity result = productService.createProduct(request);
        assertThat(result.getProductName()).isEqualTo("Cà phê sữa");
        assertThat(result.getProductPrice()).isEqualTo(25000d);
        assertThat(result.getUnit().getUnitName()).isEqualTo("Ly");
    }

    @Test
    @DisplayName("Lấy sản phẩm theo id thành công")
    void testGetProductById_Success() {
        ProductEntity p = new ProductEntity();
        p.setId(1);
        p.setProductName("Cà phê đen");
        when(productRepository.findById(1)).thenReturn(Optional.of(p));
        ProductEntity result = productService.getProductById(1);
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Cà phê đen");
    }

    @Test
    @DisplayName("Lấy sản phẩm theo id không tồn tại")
    void testGetProductById_NotFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.getProductById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy hàng hóa");
    }

    @Test
    @DisplayName("Lấy danh sách sản phẩm chưa xóa")
    void testGetAllProducts() {
        ProductEntity p1 = new ProductEntity();
        p1.setId(1);
        p1.setProductName("Cà phê sữa");
        ProductEntity p2 = new ProductEntity();
        p2.setId(2);
        p2.setProductName("Cà phê đen");
        List<ProductEntity> mockList = Arrays.asList(p1, p2);
        when(productRepository.findAllByIsDeleted(false)).thenReturn(mockList);
        List<ProductEntity> result = productService.getAllProducts();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProductName()).isEqualTo("Cà phê sữa");
    }
}

package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.impl.ImportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ImportServiceTest {
    @Mock
    private ImportRepository importRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductService productService;
    @InjectMocks
    private ImportServiceImpl importService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Tạo đơn nhập thành công")
    void testCreateImport_Success() {
        CreateImportRequest request = new CreateImportRequest();
        request.setImportDate(LocalDate.now());
        request.setQuantity(10);
        request.setProductId(1);
        ProductEntity product = new ProductEntity();
        product.setId(1);
        product.setQuantity(5);
        when(productService.getProductById(1)).thenReturn(product);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);
        ImportEntity saved = new ImportEntity();
        saved.setId(1);
        when(importRepository.save(any(ImportEntity.class))).thenReturn(saved);
        ImportEntity result = importService.createImport(request);
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Cập nhật đơn nhập không tồn tại")
    void testUpdateImport_NotFound() {
        UpdateImportRequest request = new UpdateImportRequest();
        request.setId(99);
        when(importRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> importService.updateImport(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy đơn nhập");
    }

    @Test
    @DisplayName("Lấy đơn nhập theo id thành công")
    void testGetImportById_Success() {
        ImportEntity entity = new ImportEntity();
        entity.setId(1);
        when(importRepository.findById(1)).thenReturn(Optional.of(entity));
        ImportEntity result = importService.getImportById(1);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
    }
}

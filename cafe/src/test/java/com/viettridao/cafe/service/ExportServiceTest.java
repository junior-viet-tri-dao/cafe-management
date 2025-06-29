package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.impl.ExportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ExportServiceTest {
    @Mock
    private ExportRepository exportRepository;
    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ExportServiceImpl exportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Tạo đơn xuất thành công")
    void testCreateExport_Success() {
        CreateExportRequest request = new CreateExportRequest();
        request.setExportDate(LocalDate.now());
        request.setQuantity(5);
        request.setProductId(1);
        ProductEntity product = new ProductEntity();
        product.setId(1);
        product.setQuantity(10);
        when(productService.getProductById(1)).thenReturn(product);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);
        ExportEntity saved = new ExportEntity();
        saved.setId(1);
        when(exportRepository.save(any(ExportEntity.class))).thenReturn(saved);
        ExportEntity result = exportService.createExport(request);
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Tạo đơn xuất thất bại do không đủ số lượng")
    void testCreateExport_NotEnoughQuantity() {
        CreateExportRequest request = new CreateExportRequest();
        request.setExportDate(LocalDate.now());
        request.setQuantity(20);
        request.setProductId(1);
        ProductEntity product = new ProductEntity();
        product.setId(1);
        product.setQuantity(5);
        when(productService.getProductById(1)).thenReturn(product);
        assertThatThrownBy(() -> exportService.createExport(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Không đủ số lượng");
    }
}

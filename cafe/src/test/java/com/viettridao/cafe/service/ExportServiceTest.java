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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExportServiceTest {
    @MockBean
    private ExportRepository exportRepository;
    @MockBean
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ExportServiceImpl exportService;

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

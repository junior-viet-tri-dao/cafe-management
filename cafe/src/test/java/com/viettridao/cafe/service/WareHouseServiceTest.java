
package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.response.warehouse.WareHousePageResponse;
// import com.viettridao.cafe.dto.response.warehouse.WareHouseResponse;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.service.impl.WareHouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class WareHouseServiceTest {
    @MockBean
    private ImportRepository importRepository;
    @MockBean
    private ExportRepository exportRepository;

    @Autowired
    private WareHouseServiceImpl wareHouseService;

    @Test
    @DisplayName("Lấy danh sách kho thành công")
    void testGetAllWareHouses_Success() {
        // Mock ImportEntity

        ProductEntity product1 = new ProductEntity();
        product1.setProductName("Cà phê sữa");
        product1.setProductPrice(20000.0);
        UnitEntity unit1 = new UnitEntity();
        unit1.setUnitName("Ly");
        product1.setUnit(unit1);
        ImportEntity import1 = new ImportEntity();
        import1.setId(1);
        import1.setProduct(product1);
        import1.setImportDate(java.time.LocalDate.now());
        import1.setQuantity(10);
        import1.setIsDeleted(false);

        // Mock ExportEntity
        ProductEntity product2 = new ProductEntity();
        product2.setProductName("Cà phê đen");
        product2.setProductPrice(18000.0);
        UnitEntity unit2 = new UnitEntity();
        unit2.setUnitName("Ly");
        product2.setUnit(unit2);
        ExportEntity export1 = new ExportEntity();
        export1.setId(2);
        export1.setProduct(product2);
        export1.setExportDate(java.time.LocalDate.now());
        export1.setQuantity(5);
        export1.setIsDeleted(false);

        List<ImportEntity> importList = Arrays.asList(import1);
        List<ExportEntity> exportList = Arrays.asList(export1);

        Page<ImportEntity> importPage = new PageImpl<>(importList, PageRequest.of(0, 5), 1);
        Page<ExportEntity> exportPage = new PageImpl<>(exportList, PageRequest.of(0, 5), 1);

        when(importRepository.findByIsDeletedFalse(any(Pageable.class))).thenReturn(importPage);
        when(exportRepository.findByIsDeletedFalse(any(Pageable.class))).thenReturn(exportPage);

        WareHousePageResponse result = wareHouseService.getAllWareHouses(null, 0, 5);
        assertThat(result.getWarehouses()).hasSize(2);
        // Kiểm tra tên sản phẩm đầu tiên là nhập kho
        assertThat(result.getWarehouses().get(0).getProductName()).isIn("Cà phê sữa", "Cà phê đen");
        assertThat(result.getWarehouses().get(1).getProductName()).isIn("Cà phê sữa", "Cà phê đen");
    }
}

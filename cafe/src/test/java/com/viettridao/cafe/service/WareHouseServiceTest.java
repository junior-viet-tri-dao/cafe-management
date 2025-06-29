package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.response.warehouse.WareHousePageResponse;
import com.viettridao.cafe.dto.response.warehouse.WareHouseResponse;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.service.impl.WareHouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class WareHouseServiceTest {
    @Mock
    private ImportRepository importRepository;
    @Mock
    private ExportRepository exportRepository;
    @InjectMocks
    private WareHouseServiceImpl wareHouseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Lấy danh sách kho thành công")
    void testGetAllWareHouses_Success() {
        WareHouseResponse w1 = new WareHouseResponse();
        w1.setProductName("Cà phê sữa");
        WareHouseResponse w2 = new WareHouseResponse();
        w2.setProductName("Cà phê đen");
        List<WareHouseResponse> mockList = Arrays.asList(w1, w2);
        Page<WareHouseResponse> page = new PageImpl<>(mockList, PageRequest.of(0, 5), 2);
        when(importRepository.getAllWarehouses(any(), any(Pageable.class))).thenReturn(page);
        WareHousePageResponse result = wareHouseService.getAllWareHouses(null, 0, 5);
        assertThat(result.getWarehouses()).hasSize(2);
        assertThat(result.getWarehouses().get(0).getProductName()).isEqualTo("Cà phê sữa");
    }
}

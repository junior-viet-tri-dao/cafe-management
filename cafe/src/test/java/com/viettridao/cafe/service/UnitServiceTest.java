package com.viettridao.cafe.service;

import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.impl.UnitServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class UnitServiceTest {
    @MockBean
    private UnitRepository unitRepository;

    @Autowired
    private UnitServiceImpl unitService;

    @Test
    @DisplayName("Lấy danh sách đơn vị tính thành công")
    void testGetAllUnits_Success() {
        UnitEntity u1 = new UnitEntity();
        u1.setId(1);
        u1.setUnitName("Ly");
        UnitEntity u2 = new UnitEntity();
        u2.setId(2);
        u2.setUnitName("Kg");
        List<UnitEntity> mockList = Arrays.asList(u1, u2);
        when(unitRepository.findAllByIsDeleted(false)).thenReturn(mockList);
        List<UnitEntity> result = unitService.getAllUnits();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUnitName()).isEqualTo("Ly");
    }

    @Test
    @DisplayName("Lấy đơn vị tính theo id thành công")
    void testGetUnitById_Success() {
        UnitEntity u = new UnitEntity();
        u.setId(1);
        u.setUnitName("Ly");
        when(unitRepository.findById(1)).thenReturn(Optional.of(u));
        UnitEntity result = unitService.getUnitById(1);
        assertThat(result).isNotNull();
        assertThat(result.getUnitName()).isEqualTo("Ly");
    }

    @Test
    @DisplayName("Lấy đơn vị tính theo id không tồn tại")
    void testGetUnitById_NotFound() {
        when(unitRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> unitService.getUnitById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy đơn vị tính");
    }
}

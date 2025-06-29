
package com.viettridao.cafe.service;

import com.viettridao.cafe.repository.PositionRepository;
import com.viettridao.cafe.service.impl.PositionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.viettridao.cafe.model.PositionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class PositionServiceTest {
    @Mock
    private PositionRepository positionRepository;
    @InjectMocks
    private PositionServiceImpl positionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Lấy danh sách chức vụ thành công")
    void testGetPositions_Success() {
        PositionEntity p1 = new PositionEntity();
        p1.setId(1);
        p1.setPositionName("Quản lý");
        p1.setSalary(10000000d);
        PositionEntity p2 = new PositionEntity();
        p2.setId(2);
        p2.setPositionName("Nhân viên");
        p2.setSalary(7000000d);
        List<PositionEntity> mockList = Arrays.asList(p1, p2);
        when(positionRepository.getAllPositions()).thenReturn(mockList);
        List<PositionEntity> result = positionService.getPositions();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPositionName()).isEqualTo("Quản lý");
        assertThat(result.get(1).getSalary()).isEqualTo(7000000d);
    }

    @Test
    @DisplayName("Lấy chức vụ theo id thành công")
    void testGetPositionById_Success() {
        PositionEntity p = new PositionEntity();
        p.setId(1);
        p.setPositionName("Quản lý");
        when(positionRepository.findById(1)).thenReturn(Optional.of(p));
        PositionEntity result = positionService.getPositionById(1);
        assertThat(result).isNotNull();
        assertThat(result.getPositionName()).isEqualTo("Quản lý");
    }

    @Test
    @DisplayName("Lấy chức vụ theo id không tồn tại")
    void testGetPositionById_NotFound() {
        when(positionRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> positionService.getPositionById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy chức vụ");
    }
}

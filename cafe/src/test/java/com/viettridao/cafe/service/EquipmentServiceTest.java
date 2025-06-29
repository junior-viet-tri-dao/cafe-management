package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.repository.EquipmentRepository;
import com.viettridao.cafe.service.impl.EquipmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class EquipmentServiceTest {
    @Mock
    private EquipmentRepository equipmentRepository;
    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Tạo thiết bị thành công")
    void testCreateEquipment_Success() {
        CreateEquipmentRequest request = new CreateEquipmentRequest();
        request.setEquipmentName("Máy pha cà phê");
        request.setQuantity(2);
        request.setPurchaseDate(java.time.LocalDate.now());
        request.setPurchasePrice(1000000.0);
        EquipmentEntity entity = new EquipmentEntity();
        entity.setId(1);
        when(equipmentRepository.save(any(EquipmentEntity.class))).thenReturn(entity);
        assertThatCode(() -> equipmentService.createEquipment(request)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Cập nhật thiết bị không tồn tại")
    void testUpdateEquipment_NotFound() {
        UpdateEquipmentRequest request = new UpdateEquipmentRequest();
        request.setId(99);
        when(equipmentRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> equipmentService.updateEquipment(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy thiết bị");
    }

    @Test
    @DisplayName("Xóa thiết bị không tồn tại")
    void testDeleteEquipment_NotFound() {
        when(equipmentRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> equipmentService.deleteEquipment(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy thiết bị");
    }
}

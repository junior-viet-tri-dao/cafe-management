package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentPageResponse;
import com.viettridao.cafe.model.EquipmentEntity;

import java.util.List;
//fefefe
public interface EquipmentService {
    List<EquipmentEntity> getAllEquipments();
    EquipmentEntity createEquipment(CreateEquipmentRequest request);
    void deleteEquipment(Integer id);
    EquipmentEntity getEquipmentById(Integer id);
    void updateEquipment(UpdateEquipmentRequest request);
    EquipmentPageResponse getAllEquipmentsPage(int page, int size);
}

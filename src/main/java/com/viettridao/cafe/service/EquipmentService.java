package com.viettridao.cafe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.model.EquipmentEntity;

public interface EquipmentService {
    Page<EquipmentResponse> getAllEquipments(Pageable pageable);

    Page<EquipmentResponse> searchByName(Pageable pageable, String keyword);

    EquipmentEntity createEquipment(CreateEquipmentRequest request);

    EquipmentResponse getEquipmentById(Integer id);

    void deleteEquipment(Integer id);

    void updateEquipment(UpdateEquipmentRequest editEquipmentRequest);
}

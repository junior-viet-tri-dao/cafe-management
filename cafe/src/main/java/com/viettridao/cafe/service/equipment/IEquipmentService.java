package com.viettridao.cafe.service.equipment;

import java.util.List;

import com.viettridao.cafe.dto.request.equipment.EquipmentCreateRequest;
import com.viettridao.cafe.dto.request.equipment.EquipmentUpdateRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;

public interface IEquipmentService {

    List<EquipmentResponse> getEquipmentAll();

    EquipmentResponse getByEquipmentById(Integer id);

    void createEquipment(EquipmentCreateRequest request);

    EquipmentUpdateRequest getUpdateForm(Integer id);

    void updateEquipment(Integer id, EquipmentUpdateRequest request);

    void deleteEquipment(Integer id);
}

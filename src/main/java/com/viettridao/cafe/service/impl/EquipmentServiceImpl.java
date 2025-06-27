package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.repository.EquipmentRepository;
import com.viettridao.cafe.service.EquipmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    @Override
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentRepository.findAll().stream()
                .map(equipmentMapper::convertToDto)
                .toList();
    }

    @Override
    public EquipmentEntity createEquipment(CreateEquipmentRequest request) {
        // Tạo entity mới từ request
        EquipmentEntity equipment = equipmentMapper.convertToEntity(request);
        return equipmentRepository.save(equipment);

    }

    @Override
    public EquipmentResponse getEquipmentById(Integer id) {
        EquipmentEntity equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with id: " + id));
        return equipmentMapper.convertToDto(equipment);
    }

    @Override
    public void deleteEquipment(Integer id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public void updateEquipment(UpdateEquipmentRequest editEquipmentRequest) {
        // Tìm thiết bị theo id
        EquipmentEntity equipment = equipmentRepository.findById(editEquipmentRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy thiết bị với id: " + editEquipmentRequest.getId()));

        // Cập nhật các trường
        equipment.setEquipmentName(editEquipmentRequest.getEquipmentName());
        equipment.setPurchasePrice(editEquipmentRequest.getPurchasePrice());
        equipment.setQuantity(editEquipmentRequest.getQuantity());
        equipment.setNotes(editEquipmentRequest.getNotes());
        // equipment.setPurchaseDate(editEquipmentRequest.getPurchaseDate());

        // Lưu lại vào DB
        equipmentRepository.save(equipment);
    }

}

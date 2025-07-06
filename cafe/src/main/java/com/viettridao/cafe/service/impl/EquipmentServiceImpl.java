package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.repository.EquipmentRepository;
import com.viettridao.cafe.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;

    @Override
    public List<EquipmentEntity> getAllEquipments() {
        return equipmentRepository.getAllEquipments();
    }

    @Override
    public EquipmentEntity createEquipment(CreateEquipmentRequest request) {
        if (request.getEquipmentName() == null || request.getEquipmentName().isBlank()) {
            throw new IllegalArgumentException("Tên thiết bị không được để trống");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        if (request.getPurchaseDate() == null || request.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày mua không được ở tương lai");
        }

        if (request.getPurchasePrice() == null || request.getPurchasePrice() <= 0) {
            throw new IllegalArgumentException("Giá mua phải lớn hơn 0");
        }

        EquipmentEntity equipmentEntity = new EquipmentEntity();
        equipmentEntity.setEquipmentName(request.getEquipmentName());
        equipmentEntity.setQuantity(request.getQuantity());
        equipmentEntity.setPurchaseDate(request.getPurchaseDate());
        equipmentEntity.setPurchasePrice(request.getPurchasePrice());
        equipmentEntity.setIsDeleted(false);

        return equipmentRepository.save(equipmentEntity);
    }

    @Override
    public void deleteEquipment(Integer id) {
        EquipmentEntity equipment = getEquipmentById(id);
        equipment.setIsDeleted(true);

        equipmentRepository.save(equipment);
    }

    @Override
    public EquipmentEntity getEquipmentById(Integer id) {
        return equipmentRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy thiết bị có id=" + id));
    }

    @Override
    public void updateEquipment(UpdateEquipmentRequest request) {
        if (request.getEquipmentName() == null || request.getEquipmentName().isBlank()) {
            throw new IllegalArgumentException("Tên thiết bị không được để trống");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        if (request.getPurchaseDate() == null || request.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày mua không được ở tương lai");
        }

        if (request.getPurchasePrice() == null || request.getPurchasePrice() <= 0) {
            throw new IllegalArgumentException("Giá mua phải lớn hơn 0");
        }

        EquipmentEntity equipmentEntity = getEquipmentById(request.getId());
        equipmentEntity.setEquipmentName(request.getEquipmentName());
        equipmentEntity.setQuantity(request.getQuantity());
        equipmentEntity.setPurchaseDate(request.getPurchaseDate());
        equipmentEntity.setPurchasePrice(request.getPurchasePrice());

        equipmentRepository.save(equipmentEntity);
    }
}

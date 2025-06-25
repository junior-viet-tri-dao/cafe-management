package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentPageResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.repository.EquipmentRepository;
import com.viettridao.cafe.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    @Override
    public List<EquipmentEntity> getAllEquipments() {
        return equipmentRepository.getAllEquipments();
    }

    @Transactional
    @Override
    public EquipmentEntity createEquipment(CreateEquipmentRequest request) {
        EquipmentEntity equipmentEntity = new EquipmentEntity();
        equipmentEntity.setEquipmentName(request.getEquipmentName());
        equipmentEntity.setQuantity(request.getQuantity());
        equipmentEntity.setPurchaseDate(request.getPurchaseDate());
        equipmentEntity.setPurchasePrice(request.getPurchasePrice());
        equipmentEntity.setIsDeleted(false);

        return equipmentRepository.save(equipmentEntity);
    }

    @Transactional
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

    @Transactional
    @Override
    public void updateEquipment(UpdateEquipmentRequest request) {
        EquipmentEntity equipmentEntity = getEquipmentById(request.getId());
        equipmentEntity.setEquipmentName(request.getEquipmentName());
        equipmentEntity.setQuantity(request.getQuantity());
        equipmentEntity.setPurchaseDate(request.getPurchaseDate());
        equipmentEntity.setPurchasePrice(request.getPurchasePrice());

        equipmentRepository.save(equipmentEntity);
    }

    @Override
    public EquipmentPageResponse getAllEquipmentsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EquipmentEntity> equipmentEntities = equipmentRepository.getAllEquipmentsByPage(pageable);

        EquipmentPageResponse equipmentPageResponse = new EquipmentPageResponse();
        equipmentPageResponse.setPageNumber(equipmentEntities.getNumber());
        equipmentPageResponse.setTotalElements(equipmentEntities.getTotalElements());
        equipmentPageResponse.setTotalPages(equipmentEntities.getTotalPages());
        equipmentPageResponse.setPageSize(equipmentEntities.getSize());
        equipmentPageResponse.setEquipments(equipmentMapper.toEquipmentResponseList(equipmentEntities.getContent()));

        return equipmentPageResponse;
    }
}

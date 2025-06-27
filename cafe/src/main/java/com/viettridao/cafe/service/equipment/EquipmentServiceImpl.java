package com.viettridao.cafe.service.equipment;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.equipment.EquipmentCreateRequest;
import com.viettridao.cafe.dto.request.equipment.EquipmentUpdateRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.repository.EquipmentRepository;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements IEquipmentService {

    private final EquipmentRepository equipmentReposioty;
    private final EquipmentMapper equipmentMapper;

    @Override
    public List<EquipmentResponse> getEquipmentAll() {

        return equipmentReposioty.findAllByDeletedFalse()
                .stream()
                .map(equipmentMapper::toResponse)
                .toList();

    }

    @Override
    public EquipmentResponse getByEquipmentById(Integer id) {

        EquipmentEntity entity = findEquipmentOrThrow(id);

        return equipmentMapper.toResponse(entity);

    }

    @Override
    @Transactional
    public void createEquipment(EquipmentCreateRequest request) {

        EquipmentEntity entity = equipmentMapper.toEntity(request);
        equipmentReposioty.save(entity);

    }

    @Override
    public EquipmentUpdateRequest getUpdateForm(Integer id) {

        return equipmentMapper.toUpdateRequest(findEquipmentOrThrow(id));

    }

    @Override
    @Transactional
    public void updateEquipment(Integer id, EquipmentUpdateRequest request) {

        EquipmentEntity existing = findEquipmentOrThrow(id);

        equipmentMapper.updateEntityFromRequest(request, existing);

        equipmentReposioty.save(existing);

    }

    @Override
    @Transactional
    public void deleteEquipment(Integer id) {

        EquipmentEntity entity = findEquipmentOrThrow(id);

        entity.setDeleted(true);

        equipmentReposioty.save(entity);

    }

    // Kiếm thiết bị theo id
    private EquipmentEntity findEquipmentOrThrow(Integer id) {
        return equipmentReposioty.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị với id = " + id));
    }

}

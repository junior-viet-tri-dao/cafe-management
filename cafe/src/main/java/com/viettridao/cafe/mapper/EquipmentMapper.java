package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.ImportResponse;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.model.ImportEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EquipmentMapper {
    private final ModelMapper modelMapper;

    //mapper bằng tay
    public EquipmentResponse toResponse(EquipmentEntity entity) {
        EquipmentResponse equipmentResponse = new EquipmentResponse();
        equipmentResponse.setId(entity.getId());
        equipmentResponse.setEquipmentName(entity.getEquipmentName());
        equipmentResponse.setNotes(entity.getNotes());
        equipmentResponse.setPurchaseDate(entity.getPurchaseDate());
        equipmentResponse.setPurchasePrice(entity.getPurchasePrice());
        equipmentResponse.setIsDeleted(entity.getIsDeleted());
        equipmentResponse.setQuantity(entity.getQuantity());

        List<ImportResponse> list = new ArrayList<>();
        if(entity.getImports() != null) {
            for(ImportEntity importEntity : entity.getImports()) {
                ImportResponse importResponse = new ImportResponse();
                importResponse.setId(importEntity.getId());
                importResponse.setImportDate(importEntity.getImportDate());
                list.add(importResponse);
            }
        }
        equipmentResponse.setImports(list);

        return equipmentResponse;
    }

    //mapper thư viện
    public EquipmentResponse toEquipmentResponse(EquipmentEntity entity) {
        EquipmentResponse equipmentResponse = new EquipmentResponse();
        modelMapper.map(entity, equipmentResponse);

        if(entity.getImports() != null) {
            equipmentResponse.setImports(entity.getImports().stream().map(e ->
                    modelMapper.map(e, ImportResponse.class)).toList());
        }

        return equipmentResponse;
    }

    public List<EquipmentResponse> toEquipmentResponseList(List<EquipmentEntity> entities) {
        return entities.stream().map(this::toEquipmentResponse).toList();
    }
}

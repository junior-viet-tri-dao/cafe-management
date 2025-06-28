package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.model.ImportEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportMapper {
    private final ModelMapper modelMapper;

    public ImportResponse toImportResponse(ImportEntity importEntity){
        ImportResponse importResponse = new ImportResponse();
        modelMapper.map(importEntity, importResponse);

        if(importEntity.getProduct() != null){
            importResponse.setProductId(importEntity.getProduct().getId());
            importResponse.setProductName(importEntity.getProduct().getProductName());
            importResponse.setProductPrice(importEntity.getProduct().getProductPrice());
        }

        if(importEntity.getProduct().getUnit() != null){
            importResponse.setUnitName(importEntity.getProduct().getUnit().getUnitName());
        }

        return importResponse;
    }

    public List<ImportResponse> toImportResponseList(List<ImportEntity> importEntityList){
        return importEntityList.stream().map(this::toImportResponse).toList();
    }
}

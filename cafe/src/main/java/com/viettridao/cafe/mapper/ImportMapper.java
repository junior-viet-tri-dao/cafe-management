package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.model.ImportEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper cho thực thể Import và DTO ImportResponse.
 * Chuyển đổi dữ liệu giữa ImportEntity và ImportResponse.
 */
@Component
@RequiredArgsConstructor
public class ImportMapper {
    private final ModelMapper modelMapper;

    /**
     * Chuyển đổi từ ImportEntity sang ImportResponse.
     *
     * @param importEntity Thực thể ImportEntity cần chuyển đổi.
     * @return Đối tượng ImportResponse tương ứng.
     */
    public ImportResponse toImportResponse(ImportEntity importEntity) {
        ImportResponse importResponse = new ImportResponse();
        modelMapper.map(importEntity, importResponse);

        if (importEntity.getProduct() != null) {
            importResponse.setProductId(importEntity.getProduct().getId());
            importResponse.setProductName(importEntity.getProduct().getProductName());
            importResponse.setProductPrice(importEntity.getProduct().getProductPrice());
        }

        if (importEntity.getProduct() != null && importEntity.getProduct().getUnit() != null) {
            importResponse.setUnitName(importEntity.getProduct().getUnit().getUnitName());
        }

        return importResponse;
    }

    /**
     * Chuyển đổi danh sách ImportEntity sang danh sách ImportResponse.
     *
     * @param importEntityList Danh sách thực thể ImportEntity cần chuyển đổi.
     * @return Danh sách đối tượng ImportResponse tương ứng.
     */
    public List<ImportResponse> toImportResponseList(List<ImportEntity> importEntityList) {
        return importEntityList.stream().map(this::toImportResponse).toList();
    }
}

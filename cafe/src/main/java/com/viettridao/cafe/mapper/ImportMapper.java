package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.request.imports.ImportUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.viettridao.cafe.dto.request.imports.ImportCreateRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;

@Mapper(componentModel = "spring")
public interface ImportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "mapEmployeeId")
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductId")
    ImportEntity toEntity(ImportCreateRequest request);

    ImportResponse toResponse(ImportEntity entity);

    ImportUpdateRequest toUpdateRequest(ImportEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromRequest(ImportUpdateRequest request, @MappingTarget ImportEntity entity);

    @Named("mapEmployeeId")
    default EmployeeEntity mapEmployeeId(Integer employeeId) {
        if (employeeId == null) return null;
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(employeeId);
        return employee;
    }

    @Named("mapEquipmentId")
    default EquipmentEntity mapEquipmentId(Integer equipmentId) {
        if (equipmentId == null) return null;
        EquipmentEntity equipment = new EquipmentEntity();
        equipment.setId(equipmentId);
        return equipment;
    }

    @Named("mapProductId")
    default ProductEntity mapProductId(Integer productId) {
        if (productId == null) return null;
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        return product;
    }

}

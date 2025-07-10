package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.export.UpdateExportRequest;
import com.viettridao.cafe.dto.response.export.ExportResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ProductEntity;

@Mapper(componentModel = "spring")
public interface ExportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "mapEmployeeId")
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductId")
    ExportEntity toEntity(CreateExportRequest request);

    ExportResponse toResponse(ExportEntity entity);

//    @Mapping(target = "employeeId", source = "employee.id")
//    @Mapping(target = "productId", source = "product.id")
    UpdateExportRequest toUpdateRequest(ExportEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    void updateEntityFromRequest(UpdateExportRequest request, @MappingTarget ExportEntity entity);

    @Named("mapEmployeeId")
    default EmployeeEntity mapEmployeeId(Integer employeeId) {
        if (employeeId == null)
            return null;
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(employeeId);
        return employee;
    }

    @Named("mapProductId")
    default ProductEntity mapProductId(Integer productId) {
        if (productId == null)
            return null;
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        return product;
    }
}
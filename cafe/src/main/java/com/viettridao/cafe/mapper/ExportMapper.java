package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.request.export.ExportUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.ZoneId;

import com.viettridao.cafe.dto.request.export.ExportCreateRequest;
import com.viettridao.cafe.dto.response.export.ExportResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ProductEntity;

@Mapper(componentModel = "spring")
public interface ExportMapper {

    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "mapEmployeeId")
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductId")
    ExportEntity toEntity(ExportCreateRequest request);

    ExportResponse toResponse(ExportEntity entity);

    @Mapping(target = "totalAmount", source = "totalExportAmount")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "productId", source = "product.id")
    ExportUpdateRequest toUpdateRequest(ExportEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "totalExportAmount", source = "totalAmount")
    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "mapEmployeeId")
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductId")
    void updateEntityFromRequest(ExportUpdateRequest request, @MappingTarget ExportEntity entity);

    @Named("mapEmployeeId")
    default EmployeeEntity mapEmployeeId(Integer employeeId) {
        if (employeeId == null) return null;
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(employeeId);
        return employee;
    }

    @Named("mapProductId")
    default ProductEntity mapProductId(Integer productId) {
        if (productId == null) return null;
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        return product;
    }
}

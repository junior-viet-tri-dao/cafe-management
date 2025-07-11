package com.viettridao.cafe.mapper;

import java.time.LocalDate;
import java.time.ZoneId;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;

/**
 * Mapper cho thực thể Import và DTO.
 * Chuyển đổi dữ liệu giữa ImportEntity và DTO.
 */
@Mapper(componentModel = "spring")
public interface ImportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "mapEmployeeId")
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductId")
    ImportEntity toEntity(CreateImportRequest request);

    ImportResponse toResponse(ImportEntity entity);

//    @Mapping(target = "employeeId", source = "employee.id")
//    @Mapping(target = "productId", source = "product.id")
    UpdateImportRequest toUpdateRequest(ImportEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    void updateEntityFromRequest(UpdateImportRequest request, @MappingTarget ImportEntity entity);

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

package com.viettridao.cafe.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;

@Mapper(componentModel = "spring")
public interface ImportMapper {

	@Mappings({ @Mapping(source = "product.id", target = "productId"),
			@Mapping(source = "product.productName", target = "productName", defaultValue = "Không xác định"),
			@Mapping(source = "employee.fullName", target = "employeeName", defaultValue = "Không xác định"),
			@Mapping(source = "price", target = "price") })
	ImportResponse toDto(ImportEntity entity);

	@Mapping(target = "id", ignore = true) // nếu có id tự sinh
	@Mapping(target = "employee", ignore = true)
	@Mapping(target = "product", ignore = true)
	@Mapping(target = "totalAmount", ignore = true) // sẽ set thủ công
	ImportEntity fromRequest(ImportRequest request);

	// MapStruct không tính toán logic phức tạp, nên cần post-processing:
	@AfterMapping
	default void afterFromRequest(ImportRequest request, @MappingTarget ImportEntity entity) {
		if (request.getProductId() != null) {
			ProductEntity product = new ProductEntity();
			product.setId(request.getProductId());
			entity.setProduct(product);
		}

		// Tính totalAmount = quantity * price
		if (request.getQuantity() != null && request.getPrice() != null) {
			entity.setTotalAmount(request.getQuantity() * request.getPrice());
		}
	}
}

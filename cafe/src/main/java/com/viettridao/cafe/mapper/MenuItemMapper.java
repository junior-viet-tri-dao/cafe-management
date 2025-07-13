package com.viettridao.cafe.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.MenuKey;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ProductRepository;

@Mapper(componentModel = "spring")
public abstract class MenuItemMapper {

	@Autowired
	protected ProductRepository productRepository;

	@Mapping(target = "isDeleted", constant = "false")
	@Mapping(target = "menuDetails", ignore = true) // sẽ xử lý thủ công
	public abstract MenuItemEntity fromRequest(MenuItemRequest dto);

	public abstract MenuItemResponse toDto(MenuItemEntity entity);

	@AfterMapping
	protected void afterMapping(MenuItemRequest dto, @MappingTarget MenuItemEntity entity) {
		List<MenuDetailEntity> details = dto.getIngredients().stream().map(ingredient -> {
			ProductEntity product = productRepository.findByProductNameIgnoreCase(ingredient.getProductName())
					.orElseGet(() -> {
						ProductEntity newProduct = new ProductEntity();
						newProduct.setProductName(ingredient.getProductName());
						return productRepository.save(newProduct);
					});

			MenuDetailEntity detail = new MenuDetailEntity();
			detail.setProduct(product);
			detail.setQuantity(ingredient.getQuantity());
			detail.setUnitName(ingredient.getUnitName());
			detail.setMenuItem(entity);
			detail.setIsDeleted(false);
			detail.setId(new MenuKey(product.getId(), null));

			return detail;
		}).collect(Collectors.toList());

		entity.setMenuDetails(details);
	}

	// ✅ Hàm bổ sung để cập nhật entity từ request
	public void updateEntityFromRequest(MenuItemRequest request, @MappingTarget MenuItemEntity entity) {
		entity.setItemName(request.getItemName());
		entity.setCurrentPrice(request.getCurrentPrice());
		entity.setIsDeleted(false);

		// Cập nhật lại danh sách nguyên liệu
		List<MenuDetailEntity> details = request.getIngredients().stream().map(ingredient -> {
			ProductEntity product = productRepository.findByProductNameIgnoreCase(ingredient.getProductName())
					.orElseGet(() -> {
						ProductEntity newProduct = new ProductEntity();
						newProduct.setProductName(ingredient.getProductName());
						return productRepository.save(newProduct);
					});

			MenuDetailEntity detail = new MenuDetailEntity();
			detail.setProduct(product);
			detail.setQuantity(ingredient.getQuantity());
			detail.setUnitName(ingredient.getUnitName());
			detail.setMenuItem(entity);
			detail.setIsDeleted(false);
			detail.setId(new MenuKey(product.getId(), null));

			return detail;
		}).collect(Collectors.toList());

		entity.setMenuDetails(details);
	}
}

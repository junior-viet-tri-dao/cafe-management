package com.viettridao.cafe.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.MenuKey;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ProductRepository;

@Component
public class MenuItemMapper extends BaseMapper<MenuItemEntity, MenuItemRequest, MenuItemResponse> {

	private final ProductRepository productRepository;

	// ✅ Gọi rõ ràng constructor cha
	public MenuItemMapper(ModelMapper modelMapper, ProductRepository productRepository) {
		super(modelMapper, MenuItemEntity.class, MenuItemRequest.class, MenuItemResponse.class);
		this.productRepository = productRepository;
	}

	@Override
	public MenuItemEntity fromRequest(MenuItemRequest dto) {
		MenuItemEntity entity = super.fromRequest(dto);

		List<MenuDetailEntity> detailEntities = dto.getIngredients().stream().map(ingredient -> {
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

		entity.setMenuDetails(detailEntities);
		entity.setIsDeleted(false);
		return entity;
	}
}

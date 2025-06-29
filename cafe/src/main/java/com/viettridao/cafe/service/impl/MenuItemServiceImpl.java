package com.viettridao.cafe.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.menu.MenuDetailRequest;
import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.MenuItemService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

	private final MenuItemRepository menuItemRepository;
	private final ProductRepository productRepository;
	private final MenuItemMapper menuItemMapper;

	@Override
	@Transactional
	public MenuItemResponse create(MenuItemRequest request) {
		if (menuItemRepository.existsByItemNameAndIsDeletedFalse(request.getItemName())) {
			throw new IllegalArgumentException("Tên món đã tồn tại.");
		}

		// Bước 1: tạo entity thô (chưa có details)
		MenuItemEntity entity = new MenuItemEntity();
		entity.setItemName(request.getItemName());
		entity.setCurrentPrice(request.getCurrentPrice());
		entity.setIsDeleted(false);

		// Lưu trước để có ID cho MenuKey
		entity = menuItemRepository.save(entity);

		// Bước 2: ánh xạ details
		List<MenuDetailEntity> detailEntities = new ArrayList<>();
		for (MenuDetailRequest detailReq : request.getMenuDetails()) {
			ProductEntity product = productRepository.findById(detailReq.getProductId())
					.orElseThrow(() -> new EntityNotFoundException(
							"Không tìm thấy nguyên liệu với ID: " + detailReq.getProductId()));

			MenuDetailEntity detail = menuItemMapper.toMenuDetailEntity(detailReq, product, entity);
			detailEntities.add(detail);
		}

		// Gán lại vào entity và lưu
		entity.setMenuDetails(detailEntities);
		return menuItemMapper.toDto(menuItemRepository.save(entity));
	}

	@Override
	@Transactional
	public MenuItemResponse update(Integer id, MenuItemRequest request) {
		MenuItemEntity entity = menuItemRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new EntityNotFoundException("Không tìm thấy món ăn với ID: " + id));

		// Cập nhật cơ bản
		entity.setItemName(request.getItemName());
		entity.setCurrentPrice(request.getCurrentPrice());

		// Xóa danh sách cũ (hoặc mark deleted nếu soft-delete detail)
		entity.getMenuDetails().clear();

		// Tạo danh sách mới
		List<MenuDetailEntity> newDetails = new ArrayList<>();
		for (MenuDetailRequest detailReq : request.getMenuDetails()) {
			ProductEntity product = productRepository.findById(detailReq.getProductId())
					.orElseThrow(() -> new EntityNotFoundException(
							"Không tìm thấy nguyên liệu với ID: " + detailReq.getProductId()));
			MenuDetailEntity detail = menuItemMapper.toMenuDetailEntity(detailReq, product, entity);
			newDetails.add(detail);
		}

		entity.setMenuDetails(newDetails);
		return menuItemMapper.toDto(menuItemRepository.save(entity));
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		MenuItemEntity entity = menuItemRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new EntityNotFoundException("Không tìm thấy món ăn với ID: " + id));
		entity.setIsDeleted(true);
		menuItemRepository.save(entity);
	}

	@Override
	public MenuItemResponse getById(Integer id) {
		MenuItemEntity entity = menuItemRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new EntityNotFoundException("Không tìm thấy món ăn với ID: " + id));
		return menuItemMapper.toDto(entity);
	}

	@Override
	public List<MenuItemResponse> getAll() {
		return menuItemMapper.toDtoList(menuItemRepository.findAllByIsDeletedFalse());
	}

	@Override
	public List<MenuItemResponse> search(String keyword) {
		return menuItemMapper
				.toDtoList(menuItemRepository.findByItemNameContainingIgnoreCaseAndIsDeletedFalse(keyword));
	}

	@Override
	public Page<MenuItemResponse> getAllPaged(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return menuItemRepository.findAllByIsDeletedFalse(pageable).map(menuItemMapper::toDto);
	}

	@Override
	public Page<MenuItemResponse> searchPaged(String keyword, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return menuItemRepository.findByItemNameContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable)
				.map(menuItemMapper::toDto);
	}
}

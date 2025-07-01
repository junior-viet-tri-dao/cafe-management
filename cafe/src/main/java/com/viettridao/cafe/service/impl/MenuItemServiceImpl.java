package com.viettridao.cafe.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.service.MenuItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

	private final MenuItemRepository menuItemRepository;
	private final MenuItemMapper menuItemMapper;

	@Override
	public void add(MenuItemRequest request) {
		if (menuItemRepository.existsByItemNameAndIsDeletedFalse(request.getItemName())) {
			throw new RuntimeException("Tên món đã tồn tại: " + request.getItemName());
		}

		MenuItemEntity entity = menuItemMapper.fromRequest(request);
		menuItemRepository.save(entity);
	}

	@Override
	public void update(Integer id, MenuItemRequest request) {
		MenuItemEntity entity = menuItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy món cần cập nhật"));

		// Cập nhật thông tin
		menuItemMapper.updateEntityFromRequest(request, entity);
		entity.getMenuDetails().clear(); // Xóa chi tiết cũ

		// Ánh xạ lại nguyên liệu từ request (sử dụng lại mapper)
		MenuItemEntity newEntity = menuItemMapper.fromRequest(request);
		entity.setMenuDetails(newEntity.getMenuDetails());

		menuItemRepository.save(entity);
	}

	@Override
	public void delete(Integer id) {
		MenuItemEntity entity = menuItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy món cần xóa"));

		entity.setIsDeleted(true);
		menuItemRepository.save(entity);
	}

	@Override
	public MenuItemResponse getById(Integer id) {
		MenuItemEntity entity = menuItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy món"));

		return menuItemMapper.toDto(entity);
	}

	@Override
	public MenuItemResponse getByName(String name) {
		MenuItemEntity entity = menuItemRepository.findByItemNameIgnoreCaseAndIsDeletedFalse(name)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy món: " + name));

		return menuItemMapper.toDto(entity);
	}

	@Override
	public Page<MenuItemResponse> getAll(String keyword, int page, int size) {
		return menuItemRepository
				.findByIsDeletedFalseAndItemNameContainingIgnoreCase(keyword, PageRequest.of(page, size))
				.map(menuItemMapper::toDto);
	}
}

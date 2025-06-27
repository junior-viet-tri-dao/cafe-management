package com.viettridao.cafe.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.menu_item.CreateMenuDetailRequest;
import com.viettridao.cafe.dto.request.menu_item.CreateMenuItemRequest;
import com.viettridao.cafe.dto.response.menu_item.MenuDetailResponse;
import com.viettridao.cafe.dto.response.menu_item.MenuItemResponse;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.MenuKey;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.MenuDetailRepository;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.MenuItemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuDetailRepository menuDetailRepository;
    private final MenuItemMapper menuItemMapper;
    private final ProductRepository productRepository;

    @Override
    public MenuItemResponse getMenuItemById(Integer id) {

        MenuItemEntity menuItemEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

        List<MenuDetailResponse> menuDetailResponses = menuItemEntity.getMenuDetails().stream()
                .map(menuDetailEntity -> {
                    MenuDetailResponse detailResponse = new MenuDetailResponse();
                    detailResponse.setProductId(menuDetailEntity.getProduct().getId());
                    detailResponse.setProductName(menuDetailEntity.getProduct().getProductName());
                    detailResponse.setQuantity(menuDetailEntity.getQuantity());
                    detailResponse.setUnitName(menuDetailEntity.getUnitName());
                    return detailResponse;
                }).toList();

        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItemEntity.getId());
        response.setItemName(menuItemEntity.getItemName());
        response.setCurrentPrice(menuItemEntity.getCurrentPrice());
        response.setIsDeleted(menuItemEntity.getIsDeleted());
        response.setMenuDetails(menuDetailResponses);

        return response;
    }

    @Override
    @Transactional
    public void createMenu(CreateMenuItemRequest request) {
        MenuItemEntity menuItemEntity = new MenuItemEntity();
        menuItemEntity.setItemName(request.getItemName());
        menuItemEntity.setCurrentPrice(request.getCurrentPrice());
        menuItemEntity.setIsDeleted(false);

        // Tạo món ăn và flush để sinh ID
        // 1. Tạo và lưu menuItem trước
        menuItemEntity = menuItemRepository.save(menuItemEntity);

        // 2. Tạo list MenuDetail sau khi menuItem có ID
        List<MenuDetailEntity> listMenuDetail = new ArrayList<>();
        for (CreateMenuDetailRequest detail : request.getMenuDetails()) {
            ProductEntity productEntity = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " +
                            detail.getProductId()));

            MenuDetailEntity menuDetailEntity = new MenuDetailEntity();

            // ⚠ GÁN ID TRƯỚC
            MenuKey key = new MenuKey(productEntity.getId(), menuItemEntity.getId());
            menuDetailEntity.setId(key);

            // Sau đó gán quan hệ
            menuDetailEntity.setProduct(productEntity);
            menuDetailEntity.setMenuItem(menuItemEntity);
            menuDetailEntity.setQuantity(detail.getQuantity());
            menuDetailEntity.setUnitName(detail.getUnitName());
            menuDetailEntity.setIsDeleted(false);

            listMenuDetail.add(menuDetailEntity);
        }
        menuDetailRepository.saveAll(listMenuDetail);
    }

    @Override
    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAllByIsDeletedFalse().stream()
                .map(menuItemMapper::convertToDto)
                .toList();
    }

    @Override
    public void deleteMenuItem(Integer id) {
        MenuItemEntity menuItemEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

        menuItemEntity.setIsDeleted(true);
        menuItemRepository.save(menuItemEntity);
    }

}

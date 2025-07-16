package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.dto.response.menuitem.MenuItemPageResponse;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.MenuKey;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.MenuItemDetailRepository;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.service.MenuItemService;
import com.viettridao.cafe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemDetailRepository menuItemDetailRepository;
    private final MenuItemMapper menuItemMapper;
    private final ProductService productService;

    @Override
    public MenuItemPageResponse getAllMenuItems(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItemEntity> menuItemEntityPage;

        if(StringUtils.hasText(keyword)){
            menuItemEntityPage = menuItemRepository.getAllByMenuItems(keyword, pageable);
        }else {
            menuItemEntityPage = menuItemRepository.getAllByMenuItems(pageable);
        }

        MenuItemPageResponse menuItemPageResponse = new MenuItemPageResponse();
        menuItemPageResponse.setPageNumber(menuItemEntityPage.getNumber());
        menuItemPageResponse.setPageSize(menuItemEntityPage.getSize());
        menuItemPageResponse.setTotalElements(menuItemEntityPage.getTotalElements());
        menuItemPageResponse.setTotalPages(menuItemEntityPage.getTotalPages());
        menuItemPageResponse.setItems(menuItemMapper.toListMenuItemResponse(menuItemEntityPage.getContent()));

        return menuItemPageResponse;
    }

    @Transactional
    @Override
    public MenuItemEntity createMenuItem(MenuItemCreateRequest request) {
        MenuItemEntity menuItemEntity = new MenuItemEntity();
        menuItemEntity.setItemName(request.getItemName());
        menuItemEntity.setCurrentPrice(request.getCurrentPrice());
        menuItemEntity.setIsDeleted(false);

        menuItemEntity = menuItemRepository.save(menuItemEntity);

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            List<MenuDetailEntity> menuDetailList = new ArrayList<>();
            for (var e : request.getDetails()) {
                ProductEntity product = productService.getProductById(e.getProductId());

                MenuKey id = new MenuKey();
                id.setIdMenuItem(menuItemEntity.getId());
                id.setIdProduct(product.getId());

                MenuDetailEntity menuDetailEntity = new MenuDetailEntity();
                menuDetailEntity.setId(id);
                menuDetailEntity.setProduct(product);
                menuDetailEntity.setMenuItem(menuItemEntity);
                menuDetailEntity.setUnitName(e.getUnitName());
                menuDetailEntity.setQuantity(e.getQuantity());

                menuDetailList.add(menuDetailEntity);
            }

            menuItemDetailRepository.saveAll(menuDetailList);
        }

        return menuItemEntity;
    }

    @Transactional
    @Override
    public void updateMenuItem(MenuItemUpdateRequest request) {
        MenuItemEntity menuItemEntity = getMenuItemById(request.getId());
        menuItemEntity.setItemName(request.getItemName());
        menuItemEntity.setCurrentPrice(request.getCurrentPrice());

        menuItemEntity.getMenuDetails().clear();

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            List<MenuDetailEntity> menuDetailList = new ArrayList<>();
            for (var e : request.getDetails()) {
                ProductEntity product = productService.getProductById(e.getProductId());

                MenuKey id = new MenuKey();
                id.setIdMenuItem(menuItemEntity.getId());
                id.setIdProduct(product.getId());

                MenuDetailEntity menuDetailEntity = new MenuDetailEntity();
                menuDetailEntity.setId(id);

                menuDetailEntity.setProduct(product);
                menuDetailEntity.setMenuItem(menuItemEntity);
                menuDetailEntity.setUnitName(e.getUnitName());
                menuDetailEntity.setQuantity(e.getQuantity());

                menuDetailList.add(menuDetailEntity);
            }
            menuItemDetailRepository.saveAll(menuDetailList);
        }

        menuItemRepository.save(menuItemEntity);
    }


    @Transactional
    @Override
    public void deleteMenuItem(Integer id) {
        MenuItemEntity menuItemEntity = getMenuItemById(id);
        menuItemEntity.setIsDeleted(true);

        menuItemRepository.save(menuItemEntity);
    }

    @Override
    public MenuItemEntity getMenuItemById(Integer id) {
        return menuItemRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy thực đơn có id=" + id));
    }

    @Override
    public List<MenuItemEntity> getAllMenuItems() {
        return menuItemRepository.findAllByIsDeletedFalse();
    }
}

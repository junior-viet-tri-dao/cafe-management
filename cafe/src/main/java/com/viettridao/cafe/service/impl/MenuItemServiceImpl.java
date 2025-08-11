package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.menu.CreateMenuItemRequest;
import com.viettridao.cafe.dto.request.menu.UpdateMenuItemRequest;
import com.viettridao.cafe.dto.request.menudetail.CreateMenuItemDetailRequest;
import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponsePage;
import com.viettridao.cafe.dto.response.promotion.PromotionResponsePage;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.MenuItemDetailRepository;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.repository.PromotionRepository;
import com.viettridao.cafe.service.MenuItemService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final ProductService productService;
    private final MenuItemDetailRepository menuItemDetailRepository;


    @Override
    public MenuItemEntity getMenuItemByID(Integer id) {
        return null;
    }

    @Override
    public MenuItemResponsePage getAllMenuItemPage(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItemEntity> menuItemEntity;
        if(StringUtils.hasText(keyword)){
            menuItemEntity = menuItemRepository.getAllMenuPageSearch(keyword,pageable);
        }
        else{
            menuItemEntity = menuItemRepository.getAllMenuPage(pageable);
        }

        MenuItemResponsePage menuItemResponsePage = new MenuItemResponsePage();
        menuItemResponsePage.setMenuPage(menuItemEntity.getContent());
        menuItemResponsePage.setPageNumber(menuItemEntity.getNumber());
        menuItemResponsePage.setPageSize(menuItemEntity.getSize());
        menuItemResponsePage.setTotalPages(menuItemEntity.getTotalPages());
        menuItemResponsePage.setTotalElements(menuItemEntity.getTotalElements());

        return menuItemResponsePage;
    }

    @Override
    public void createMenu(CreateMenuItemRequest createMenuItemRequest) {
        MenuItemEntity menuItemEntity = new MenuItemEntity();
        menuItemEntity.setCurrentPrice(createMenuItemRequest.getCurrentPrice());
        menuItemEntity.setItemName(createMenuItemRequest.getItemName());
        menuItemEntity.setIsDeleted(false);
        menuItemEntity = menuItemRepository.save(menuItemEntity);

        if(createMenuItemRequest.getMenuItemDetail() != null && !createMenuItemRequest.getMenuItemDetail().isEmpty()){
            List<MenuDetailEntity> menuDetailEntities = new ArrayList<>();

            for(CreateMenuItemDetailRequest m : createMenuItemRequest.getMenuItemDetail()){
                ProductEntity productEntity = productService.getProductById(m.getProductId());

                MenuKey id_new = new MenuKey();
                id_new.setIdMenuItem(menuItemEntity.getId());
                id_new.setIdProduct(productEntity.getId());

                MenuDetailEntity menuDetailEntity = new MenuDetailEntity();
                menuDetailEntity.setId(id_new);
                menuDetailEntity.setMenuItem(menuItemEntity);
                menuDetailEntity.setProduct(productEntity);
                menuDetailEntity.setQuantity(m.getQuantity());
                menuDetailEntity.setUnitName(m.getUnitName());
                menuDetailEntity.setIsDeleted(false);
                menuDetailEntities.add(menuDetailEntity);
            }

            menuItemDetailRepository.saveAll(menuDetailEntities);
        }

    }

    @Override
    public void deleteMenuItemById(Integer id) {
        MenuItemEntity menuItemEntity = menuItemRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy menu có ID =" + id));;
        menuItemEntity.setIsDeleted(true);
        menuItemRepository.save(menuItemEntity);
    }

    @Override
    public void updateMenuItem(UpdateMenuItemRequest request) {
        MenuItemEntity menuItemEntity = menuItemRepository.findById(request.getId()).orElseThrow(()-> new RuntimeException("Không tìm thấy menu với Id=" + request.getId()) );
        menuItemEntity.setCurrentPrice(request.getCurrentPrice());

        menuItemRepository.save(menuItemEntity);

    }

    @Override
    public List<MenuItemEntity> getAllMenuItems() {
        return menuItemRepository.getAllMenuItems();
    }

    @Override
    public MenuItemEntity getMenuItemById(Integer menuId) {
        return menuItemRepository.findById(menuId).orElseThrow(()-> new RuntimeException("Không tìm thấy menu item với id = " + menuId));
    }


}

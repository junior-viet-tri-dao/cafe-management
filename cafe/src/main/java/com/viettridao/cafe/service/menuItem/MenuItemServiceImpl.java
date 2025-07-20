package com.viettridao.cafe.service.menuItem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.menudetail.MenuDetailCreateRequest;
import com.viettridao.cafe.dto.request.menudetail.MenuDetailUpdateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.dto.response.menuitem.MenuItemResponse;
import com.viettridao.cafe.mapper.MenuDetailMapper;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.MenuKey;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements IMenuItemService{

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuDetailMapper menuDetailMapper;
    private final ProductRepository productRepository;


    @Override
    public List<MenuItemResponse> getMenuItemAll() {
        return menuItemRepository.findAllByDeletedFalse()
                .stream()
                .map(menuItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void createMenuItem(MenuItemCreateRequest request) {

        MenuItemEntity temp = menuItemMapper.toEntity(request);
        temp.setMenuDetails(null);
        MenuItemEntity savedMenuItem = menuItemRepository.save(temp);

        List<MenuDetailEntity> details = new ArrayList<>();
        for (MenuDetailCreateRequest detailRequest : request.getMenuDetails()) {
            ProductEntity product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Nguyên liệu không tồn tại: " + detailRequest.getProductId()));

            MenuDetailEntity detail = new MenuDetailEntity();
            detail.setMenuItem(savedMenuItem);
            detail.setProduct(product);

            MenuKey key = new MenuKey(savedMenuItem.getId(), product.getId());
            detail.setId(key);

            detail.setQuantity(detailRequest.getQuantity());
            detail.setUnitName(detailRequest.getUnitName());
            detail.setDeleted(false);

            details.add(detail);
        }

        savedMenuItem.setMenuDetails(details);
        menuItemRepository.save(savedMenuItem);

    }

    @Override
    public MenuItemUpdateRequest getUpdateForm(Integer id) {
        MenuItemEntity entity = findMenuItemOrThrow(id);
        MenuItemUpdateRequest dto = menuItemMapper.toUpdateRequest(entity);

        // Map thêm menuDetails
        List<MenuDetailUpdateRequest> detailDtos = new ArrayList<>();

        for (MenuDetailEntity detail : entity.getMenuDetails()) {
            if (!Boolean.TRUE.equals(detail.getDeleted())) {
                MenuDetailUpdateRequest d = new MenuDetailUpdateRequest();
                d.setProductId(detail.getProduct().getId());
                d.setQuantity(detail.getQuantity());
                d.setUnitName(detail.getUnitName());
                d.setDeleted(detail.getDeleted());
                detailDtos.add(d);
            }
        }

        dto.setMenuDetails(detailDtos);
        return dto;

    }

    @Override
    @Transactional
    public void updateMenuItem(Integer id, MenuItemUpdateRequest request) {
        MenuItemEntity entity = findMenuItemOrThrow(id);

        entity.setItemName(request.getItemName());
        entity.setCurrentPrice(request.getCurrentPrice());

        List<MenuDetailEntity> currentDetails = entity.getMenuDetails();

        // 1. XÓA những MenuDetailEntity không còn trong danh sách mới (AI)
        currentDetails.removeIf(existing ->
                request.getMenuDetails().stream()
                        .noneMatch(incoming -> incoming.getProductId().equals(existing.getProduct().getId()))
        );

        // Cập nhật só
        for (MenuDetailUpdateRequest incomingDetail : request.getMenuDetails()) {
            Integer incomingProductId = incomingDetail.getProductId();

            // Tìm xem có trong currentDetails chưa
            MenuDetailEntity existing = currentDetails.stream()
                    .filter(d -> d.getProduct().getId().equals(incomingProductId))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                // Cập nhật
                existing.setQuantity(incomingDetail.getQuantity());
                existing.setUnitName(incomingDetail.getUnitName());
                existing.setDeleted(false);
            } else {
                // Thêm mới
                ProductEntity product = productRepository.findById(incomingProductId)
                        .orElseThrow(() -> new RuntimeException("Nguyên liệu không tồn tại: " + incomingProductId));

                MenuDetailEntity newDetail = new MenuDetailEntity();
                newDetail.setId(new MenuKey(id, incomingProductId));
                newDetail.setMenuItem(entity);
                newDetail.setProduct(product);
                newDetail.setQuantity(incomingDetail.getQuantity());
                newDetail.setUnitName(incomingDetail.getUnitName());
                newDetail.setDeleted(false);

                currentDetails.add(newDetail);
            }
        }

        menuItemRepository.save(entity);
    }



    @Override
    @Transactional
    public void deleteMenuItem(Integer id) {

        MenuItemEntity entity = findMenuItemOrThrow(id);
        entity.setDeleted(true);
        menuItemRepository.save(entity);

    }

    // Kiếm thực đơn theo id
    private MenuItemEntity findMenuItemOrThrow(Integer id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thực đơn theo id = " + id));
    }
}

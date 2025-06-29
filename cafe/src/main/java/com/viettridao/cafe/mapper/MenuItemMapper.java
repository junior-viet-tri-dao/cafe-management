package com.viettridao.cafe.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettridao.cafe.dto.response.menu.MenuItemDetailResponse;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.MenuItemEntity;

@Component
public class MenuItemMapper extends BaseMapper<MenuItemEntity, Object, MenuItemResponse> {

    private final ObjectMapper objectMapper;

    public MenuItemMapper(ModelMapper modelMapper) {
        super(modelMapper, MenuItemEntity.class, Object.class, MenuItemResponse.class);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public MenuItemResponse toDto(MenuItemEntity entity) {
        MenuItemResponse response = super.toDto(entity);
        response.setItemName(entity.getItemName());
        response.setCurrentPrice(entity.getCurrentPrice());

        if (entity.getMenuDetails() != null) {
            List<MenuItemDetailResponse> detailResponses = new ArrayList<>();

            for (var detail : entity.getMenuDetails()) {
                MenuItemDetailResponse d = new MenuItemDetailResponse();
                d.setProductId(detail.getProduct().getId());
                d.setUnitName(detail.getUnitName());
                d.setQuantity(detail.getQuantity());
                detailResponses.add(d);
            }

            response.setDetails(detailResponses);

            try {
                String json = objectMapper.writeValueAsString(detailResponses);
                response.setDetailsJson(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Lỗi khi chuyển danh sách chi tiết món sang JSON", e);
            }
        }

        return response;
    }

    public List<MenuItemResponse> toDtoList(List<MenuItemEntity> entityList) {
        return entityList.stream().map(this::toDto).toList();
    }
}

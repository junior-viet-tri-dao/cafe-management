package com.viettridao.cafe.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettridao.cafe.dto.response.menuitem.MenuItemResponse;
import com.viettridao.cafe.dto.response.menuitem_detail.MenuItemDetailResponse;
import com.viettridao.cafe.model.MenuItemEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class MenuItemMapper {
    private final ModelMapper modelMapper;

    public MenuItemResponse toResponse(MenuItemEntity entity) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(entity.getId());
        response.setItemName(entity.getItemName());
        response.setCurrentPrice(entity.getCurrentPrice());

        if(entity.getMenuDetails() != null) {
            List<MenuItemDetailResponse> responseList = new ArrayList<>();
            for(var e : entity.getMenuDetails()){
                MenuItemDetailResponse detailResponse = new MenuItemDetailResponse();
                detailResponse.setProductId(e.getProduct().getId());
                detailResponse.setUnitName(e.getUnitName());
                detailResponse.setQuantity(e.getQuantity());
                responseList.add(detailResponse);
            }

            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(responseList);
                response.setDetailsJson(json);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Không convert được JSON", ex);
            }
            response.setDetails(responseList);
        }
        return response;
    }

    public List<MenuItemResponse> toListMenuItemResponse(List<MenuItemEntity> entityList) {
        return entityList.stream().map(this::toResponse).toList();
    }
}

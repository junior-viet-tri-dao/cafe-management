package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;

import com.viettridao.cafe.dto.response.menukeyresponse.MenuKeyResponse;
import com.viettridao.cafe.model.MenuKey;

@Mapper(componentModel = "spring")
public interface MenuKeyMapper {

    MenuKeyResponse toResponse(MenuKey entity);
}

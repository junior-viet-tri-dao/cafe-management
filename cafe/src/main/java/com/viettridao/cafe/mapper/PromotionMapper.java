package com.viettridao.cafe.mapper;

import java.time.LocalDate;
import java.time.ZoneId;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.viettridao.cafe.dto.request.promotion.PromotionCreateRequest;
import com.viettridao.cafe.dto.request.promotion.PromotionUpdateRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.model.PromotionEntity;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "invoices", ignore = true)
    PromotionEntity toEntity(PromotionCreateRequest request);

    PromotionResponse toResponse(PromotionEntity entity);

    PromotionUpdateRequest toUpdateRequest(PromotionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromRequest(PromotionUpdateRequest request, @MappingTarget PromotionEntity entity);

    default LocalDate map(java.util.Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

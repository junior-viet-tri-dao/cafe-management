package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.viettridao.cafe.dto.request.invoice.InvoiceRequest;
import com.viettridao.cafe.dto.response.invoice.InvoiceResponse;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.PromotionEntity;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    //@Mapping(target = "promotion", source = "promotionId", qualifiedByName = "mapPromotionId")
    @Mapping(target = "invoiceDetails", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    InvoiceEntity toEntity(InvoiceRequest request);

    InvoiceResponse toResponse(InvoiceEntity entity);

    @Named("mapPromotionId")
    default PromotionEntity mapPromotionId(Integer promotionId) {
        if (promotionId == null) return null;
        PromotionEntity promotion = new PromotionEntity();
        promotion.setId(promotionId);
        return promotion;
    }

}

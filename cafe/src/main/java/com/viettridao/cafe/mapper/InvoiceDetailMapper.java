package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.viettridao.cafe.dto.request.invoicedetail.InvoiceDetailRequest;
import com.viettridao.cafe.dto.response.invoicedetail.InvoiceDetailResponse;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.MenuItemEntity;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {

    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "invoice", source = "invoiceId", qualifiedByName = "mapInvoiceId")
    @Mapping(target = "menuItem", source = "menuItemId", qualifiedByName = "mapMenuItemId")
    InvoiceDetailEntity toEntity(InvoiceDetailRequest request);

    InvoiceDetailResponse toResponse(InvoiceDetailEntity entity);

    @Named("mapInvoiceId")
    default InvoiceEntity mapInvoiceId(Integer invoiceId) {
        if (invoiceId == null) return null;
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(invoiceId);
        return invoice;
    }

    @Named("mapMenuItemId")
    default MenuItemEntity mapMenuItemId(Integer menuItemId) {
        if (menuItemId == null) return null;
        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setId(menuItemId);
        return menuItem;
    }

}

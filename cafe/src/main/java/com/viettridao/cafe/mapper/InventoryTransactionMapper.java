package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.response.inventory.InventoryTransactionResponse;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;

@Mapper(componentModel = "spring")
public interface InventoryTransactionMapper {

    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.unit.unitName", target = "unitName")
    @Mapping(target = "exportDate", ignore = true)
    @Mapping(target = "type", constant = "IMPORT")
    InventoryTransactionResponse fromImport(ImportEntity importEntity);

    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.unit.unitName", target = "unitName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "totalExportAmount", target = "totalAmount")
    @Mapping(target = "importDate", ignore = true)
    @Mapping(target = "type", constant = "EXPORT")
    InventoryTransactionResponse fromExport(ExportEntity exportEntity);
}


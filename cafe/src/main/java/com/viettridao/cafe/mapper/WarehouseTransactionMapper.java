package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.response.warehouse_transaction.WarehouseTransactionResponse;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;

@Mapper(componentModel = "spring")
public interface WarehouseTransactionMapper {

    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.unit.unitName", target = "unitName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unitImportPrice", target = "unitPrice")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "importDate", target = "importDate")
    @Mapping(target = "exportDate", ignore = true)
    @Mapping(target = "type", constant = "IMPORT")
    WarehouseTransactionResponse fromImport(ImportEntity importEntity);

    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.unit.unitName", target = "unitName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unitExportPrice", target = "unitPrice")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "exportDate", target = "exportDate")
    @Mapping(target = "importDate", ignore = true)
    @Mapping(target = "type", constant = "EXPORT")
    WarehouseTransactionResponse fromExport(ExportEntity exportEntity);
}
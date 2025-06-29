package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.response.warehouse.WareHousePageResponse;
import com.viettridao.cafe.dto.response.warehouse.WareHouseResponse;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.service.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
    private final ImportRepository importRepository;
    private final ExportRepository exportRepository;

    @Override
    public WareHousePageResponse getAllWareHouses(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Lấy danh sách nhập kho
        Page<com.viettridao.cafe.model.ImportEntity> importPage = (keyword != null && !keyword.isBlank())
                ? importRepository.findByProduct_ProductNameContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable)
                : importRepository.findByIsDeletedFalse(pageable);

        // Lấy danh sách xuất kho
        Page<com.viettridao.cafe.model.ExportEntity> exportPage = (keyword != null && !keyword.isBlank())
                ? exportRepository.findByProduct_ProductNameContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable)
                : exportRepository.findByIsDeletedFalse(pageable);

        java.util.List<WareHouseResponse> warehouseList = new java.util.ArrayList<>();

        // Map nhập kho
        for (com.viettridao.cafe.model.ImportEntity imp : importPage.getContent()) {
            WareHouseResponse dto = new WareHouseResponse();
            dto.setImportId(imp.getId());
            dto.setExportId(null);
            dto.setProductName(imp.getProduct().getProductName());
            dto.setImportDate(imp.getImportDate() != null ? java.sql.Date.valueOf(imp.getImportDate()) : null);
            dto.setExportDate(null);
            dto.setQuantityImport(imp.getQuantity());
            dto.setQuantityExport(null);
            dto.setUnitName(imp.getProduct().getUnit() != null ? imp.getProduct().getUnit().getUnitName() : null);
            dto.setProductPrice(imp.getProduct().getProductPrice());
            warehouseList.add(dto);
        }

        // Map xuất kho
        for (com.viettridao.cafe.model.ExportEntity exp : exportPage.getContent()) {
            WareHouseResponse dto = new WareHouseResponse();
            dto.setImportId(null);
            dto.setExportId(exp.getId());
            dto.setProductName(exp.getProduct().getProductName());
            dto.setImportDate(null);
            dto.setExportDate(exp.getExportDate() != null ? java.sql.Date.valueOf(exp.getExportDate()) : null);
            dto.setQuantityImport(null);
            dto.setQuantityExport(exp.getQuantity());
            dto.setUnitName(exp.getProduct().getUnit() != null ? exp.getProduct().getUnit().getUnitName() : null);
            dto.setProductPrice(exp.getProduct().getProductPrice());
            warehouseList.add(dto);
        }

        // Gộp, sort theo ngày (importDate/exportDate, giảm dần)
        warehouseList.sort((a, b) -> {
            java.util.Date dateA = a.getImportDate() != null ? a.getImportDate() : a.getExportDate();
            java.util.Date dateB = b.getImportDate() != null ? b.getImportDate() : b.getExportDate();
            if (dateA == null && dateB == null)
                return 0;
            if (dateA == null)
                return 1;
            if (dateB == null)
                return -1;
            return dateB.compareTo(dateA);
        });

        // Phân trang thủ công
        int start = Math.min(page * size, warehouseList.size());
        int end = Math.min(start + size, warehouseList.size());
        java.util.List<WareHouseResponse> pagedList = warehouseList.subList(start, end);

        WareHousePageResponse response = new WareHousePageResponse();
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(warehouseList.size());
        response.setTotalPages((int) Math.ceil((double) warehouseList.size() / size));
        response.setWarehouses(pagedList);
        return response;
    }
}

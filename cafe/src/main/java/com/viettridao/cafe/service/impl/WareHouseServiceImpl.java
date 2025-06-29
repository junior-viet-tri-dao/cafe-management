package com.viettridao.cafe.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.response.warehouse.WareHousePageResponse;
import com.viettridao.cafe.dto.response.warehouse.WareHouseResponse;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.service.WareHouseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
	private final ImportRepository importRepository;
	private final ExportRepository exportRepository;

	@Override
	public WareHousePageResponse getAllWareHouses(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<WareHouseResponse> combinedList = new ArrayList<>();

		// Xử lý đơn nhập
		Page<ImportEntity> imports = importRepository
				.findByProduct_ProductNameContainingIgnoreCase(keyword != null ? keyword : "", pageable);

		for (ImportEntity i : imports.getContent()) {
			WareHouseResponse res = new WareHouseResponse();
			res.setImportId(i.getId());
			if (i.getProduct() != null) {
				res.setProductName(i.getProduct().getProductName());
				res.setUnitName(
						i.getProduct().getUnit() != null ? i.getProduct().getUnit().getUnitName() : "Không xác định");
				res.setProductPrice(i.getProduct().getProductPrice());
			} else {
				res.setProductName("Không xác định");
				res.setUnitName("Không xác định");
				res.setProductPrice(0.0);
			}
			res.setImportDate(i.getImportDate() != null ? java.sql.Date.valueOf(i.getImportDate()) : null);
			res.setQuantityImport(i.getQuantity());
			combinedList.add(res);
		}

		// Xử lý đơn xuất
		Page<ExportEntity> exports = exportRepository
				.findByProduct_ProductNameContainingIgnoreCase(keyword != null ? keyword : "", pageable);

		for (ExportEntity e : exports.getContent()) {
			WareHouseResponse res = new WareHouseResponse();
			res.setExportId(e.getId());
			if (e.getProduct() != null) {
				res.setProductName(e.getProduct().getProductName());
				res.setUnitName(
						e.getProduct().getUnit() != null ? e.getProduct().getUnit().getUnitName() : "Không xác định");
				res.setProductPrice(e.getProduct().getProductPrice());
			} else {
				res.setProductName("Không xác định");
				res.setUnitName("Không xác định");
				res.setProductPrice(0.0);
			}
			res.setExportDate(e.getExportDate() != null ? java.sql.Date.valueOf(e.getExportDate()) : null);
			res.setQuantityExport(e.getQuantity());
			combinedList.add(res);
		}

		// Sắp xếp theo ngày gần nhất (ưu tiên ngày nhập/xuất)
		combinedList.sort((a, b) -> {
			Date dateA = a.getImportDate() != null ? a.getImportDate() : a.getExportDate();
			Date dateB = b.getImportDate() != null ? b.getImportDate() : b.getExportDate();
			return dateB.compareTo(dateA); // mới nhất trước
		});

		// Phân trang thủ công
		int start = Math.min(page * size, combinedList.size());
		int end = Math.min(start + size, combinedList.size());
		List<WareHouseResponse> pageContent = combinedList.subList(start, end);

		// Trả về
		WareHousePageResponse response = new WareHousePageResponse();
		response.setWarehouses(pageContent);
		response.setPageNumber(page);
		response.setPageSize(size);
		response.setTotalElements((long) combinedList.size());
		response.setTotalPages((int) Math.ceil((double) combinedList.size() / size));

		return response;
	}
}

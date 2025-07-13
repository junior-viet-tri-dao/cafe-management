package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;

public interface ExportService {

	ExportResponse createExport(ExportRequest request);

	void exportProduct(ExportRequest request);

	List<ExportResponse> getAll();

	List<ExportResponse> getExportsByProduct(Integer productId);

	Page<ExportResponse> getExportsByProductId(Integer productId, int page, int size);
}

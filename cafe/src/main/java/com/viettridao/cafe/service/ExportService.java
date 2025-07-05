package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;

public interface ExportService {
	ExportResponse createExport(ExportRequest request);

	List<ExportResponse> getExportsByProduct(Integer productId);

	List<ExportResponse> getAll();
}

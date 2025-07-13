package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;

public interface ImportService {

	ImportResponse createImport(ImportRequest request);

	List<ImportResponse> getAll();

	List<ImportResponse> getImportsByProduct(Integer productId);

	Page<ImportResponse> getImportsByProductId(Integer productId, int page, int size);
}

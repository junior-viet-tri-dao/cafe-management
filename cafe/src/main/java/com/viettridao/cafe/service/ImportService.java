package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;

public interface ImportService {
    ImportResponse createImport(ImportRequest request);

    List<ImportResponse> getImportsByProduct(Integer productId);

    List<ImportResponse> getAll();
}



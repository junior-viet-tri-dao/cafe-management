package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.model.ImportEntity;

public interface ImportService {
    ImportEntity createImport(CreateImportRequest request);
    void updateImport(UpdateImportRequest request);
    ImportEntity getImportById(Integer id);
}

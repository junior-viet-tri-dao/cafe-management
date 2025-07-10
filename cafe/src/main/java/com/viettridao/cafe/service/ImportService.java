package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;

public interface ImportService {

    void createImport(CreateImportRequest request);

    UpdateImportRequest getUpdateForm(Integer id);

    void updateImport(Integer id, UpdateImportRequest request);

    void deleteImport(Integer id);
}
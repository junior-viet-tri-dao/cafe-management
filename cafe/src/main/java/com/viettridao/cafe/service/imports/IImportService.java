package com.viettridao.cafe.service.imports;

import com.viettridao.cafe.dto.request.imports.ImportCreateRequest;
import com.viettridao.cafe.dto.request.imports.ImportUpdateRequest;

public interface IImportService {

    void createImport(ImportCreateRequest request);

    ImportUpdateRequest getUpdateForm(Integer id);

    void updateImport(Integer id, ImportUpdateRequest request);

    void deleteImport(Integer id);
}

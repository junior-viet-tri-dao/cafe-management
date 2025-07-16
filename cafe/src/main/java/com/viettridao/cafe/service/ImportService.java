package com.viettridao.cafe.service;


import com.viettridao.cafe.dto.response.importProduct.ImportResponsePage;
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;

import com.viettridao.cafe.model.ImportEntity;


import java.util.List;

public interface ImportService {

    ImportEntity getImportbyId(Integer id);

    void createImport( CreateImportRequest request);

    void updateImport(UpdateImportRequest request);

    void deleteImportbyId(Integer id);


    ImportResponsePage getAllImportPage(String keyword, int page, int size);
}

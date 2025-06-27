package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.import_request.ImportRequest;
import com.viettridao.cafe.model.ImportEntity;

public interface ImportService {

    ImportEntity createImport(ImportRequest request, Integer employeeId);
}

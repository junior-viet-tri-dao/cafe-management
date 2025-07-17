package com.viettridao.cafe.service;

import java.time.LocalDate;
import java.util.List;

import com.viettridao.cafe.dto.request.import_request.ImportRequest;
import com.viettridao.cafe.model.ImportEntity;

public interface ImportService {

    ImportEntity createImport(ImportRequest request, Integer employeeId);

    List<ImportEntity> getAllByImportDateRange(LocalDate startDate, LocalDate endDate);
}

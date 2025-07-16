package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.export.UpdateExportRequest;
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.response.export.ExportResponsePage;
import com.viettridao.cafe.dto.response.importProduct.ImportResponsePage;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import jakarta.validation.Valid;

public interface ExportService {

    ExportEntity getExportbyId(Integer id);

    void createExport(CreateExportRequest request);

    void deleteExportbyId(Integer id);

    void updateExport( UpdateExportRequest request);

    ExportResponsePage getAllIExportPage(String keyword, int page, int size);
}

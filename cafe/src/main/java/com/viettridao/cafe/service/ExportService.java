package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.export.UpdateExportRequest;

public interface ExportService {

    void createExport(CreateExportRequest request);

    UpdateExportRequest getUpdateForm(Integer id);

    void updateExport(Integer id, UpdateExportRequest request);

    void deleteExport(Integer id);
}
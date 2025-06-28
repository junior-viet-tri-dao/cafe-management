package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.model.ExportEntity;

public interface ExportService {
    ExportEntity createExport(CreateExportRequest request);
}

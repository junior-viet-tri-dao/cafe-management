package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.export_request.ExportRequest;
import com.viettridao.cafe.model.ExportEntity;

public interface ExportService {

    ExportEntity createExport(ExportRequest request, Integer employeeId);
}

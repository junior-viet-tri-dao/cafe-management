package com.viettridao.cafe.service.exports;

import com.viettridao.cafe.dto.request.export.ExportCreateRequest;
import com.viettridao.cafe.dto.request.export.ExportUpdateRequest;

public interface IExportService {

    void createExport(ExportCreateRequest request);

    ExportUpdateRequest getUpdateForm(Integer id);

    void updateExport(Integer id, ExportUpdateRequest request);

    void deleteExport(Integer id);
}

package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reports.ReportFilterRequest;
import java.util.List;

/**
 * ReportService
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 */
public interface ReportService {
    /**
     * Tạo báo cáo (file, ví dụ PDF, Excel) dựa trên bộ lọc.
     *
     * @param request Bộ lọc báo cáo.
     * @return Mảng byte của file báo cáo được sinh ra.
     */
    byte[] generateReport(ReportFilterRequest request);

    /**
     * Lấy dữ liệu báo cáo theo bộ lọc.
     *
     * @param request Bộ lọc báo cáo.
     * @return Danh sách dữ liệu báo cáo.
     */
    List<?> getReportData(ReportFilterRequest request);
}
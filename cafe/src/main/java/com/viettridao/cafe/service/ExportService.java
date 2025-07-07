package com.viettridao.cafe.service;

// Import các thư viện cần thiết
import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.model.ExportEntity;

/**
 * Service cho thực thể ExportEntity.
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến xuất kho (Export).
 */
public interface ExportService {

    /**
     * Tạo mới một bản ghi xuất kho.
     *
     * @param request Đối tượng chứa thông tin cần thiết để tạo bản ghi xuất kho
     *                mới.
     * @return Thực thể ExportEntity vừa được tạo.
     */
    ExportEntity createExport(CreateExportRequest request);
}

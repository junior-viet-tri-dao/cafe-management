package com.viettridao.cafe.service;

// Import các thư viện cần thiết
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.model.ImportEntity;

/**
 * Service cho thực thể ImportEntity.
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến nhập kho (Import).
 */
public interface ImportService {

    /**
     * Tạo mới một bản ghi nhập kho.
     *
     * @param request Đối tượng chứa thông tin cần thiết để tạo bản ghi nhập kho
     *                mới.
     * @return Thực thể ImportEntity vừa được tạo.
     */
    ImportEntity createImport(CreateImportRequest request);

    /**
     * Cập nhật thông tin bản ghi nhập kho.
     *
     * @param request Đối tượng chứa thông tin cần cập nhật cho bản ghi nhập kho.
     */
    void updateImport(UpdateImportRequest request);

    /**
     * Lấy thông tin chi tiết của một bản ghi nhập kho dựa trên ID.
     *
     * @param id ID của bản ghi nhập kho cần lấy thông tin.
     * @return Thực thể ImportEntity tương ứng với ID.
     */
    ImportEntity getImportById(Integer id);
}

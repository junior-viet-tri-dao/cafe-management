package com.viettridao.cafe.service.impl;

// Import các thư viện cần thiết
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lớp triển khai các phương thức xử lý logic liên quan đến đơn nhập hàng.
 */
@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    // Repository quản lý dữ liệu đơn nhập hàng
    private final ImportRepository importRepository;

    // Repository quản lý dữ liệu sản phẩm
    private final ProductRepository productRepository;

    // Service xử lý logic liên quan đến sản phẩm
    private final ProductService productService;

    /**
     * Tạo mới một đơn nhập hàng.
     *
     * @param request Đối tượng chứa thông tin đơn nhập cần tạo.
     * @return Đối tượng ImportEntity đã được lưu vào cơ sở dữ liệu.
     */
    @Transactional
    @Override
    public ImportEntity createImport(CreateImportRequest request) {
        ImportEntity importEntity = new ImportEntity();
        importEntity.setImportDate(request.getImportDate());
        importEntity.setQuantity(request.getQuantity());

        // Lấy thông tin sản phẩm dựa trên ID
        ProductEntity product = productService.getProductById(request.getProductId());
        product.setQuantity(product.getQuantity() + request.getQuantity());

        productRepository.save(product);
        importEntity.setProduct(product);

        return importRepository.save(importEntity);
    }

    /**
     * Cập nhật thông tin một đơn nhập hàng.
     *
     * @param request Đối tượng chứa thông tin cần cập nhật của đơn nhập.
     */
    @Transactional
    @Override
    public void updateImport(UpdateImportRequest request) {
        ImportEntity importEntity = getImportById(request.getId());
        importEntity.setImportDate(request.getImportDate());
        importEntity.setQuantity(request.getQuantity());

        // Lấy thông tin sản phẩm dựa trên ID
        ProductEntity product = productService.getProductById(request.getProductId());
        importEntity.setProduct(product);

        importRepository.save(importEntity);
    }

    /**
     * Lấy thông tin đơn nhập hàng theo ID.
     *
     * @param id ID của đơn nhập cần tìm.
     * @return Đối tượng ImportEntity tương ứng với ID.
     * @throws RuntimeException nếu không tìm thấy đơn nhập với ID đã cho.
     */
    @Override
    public ImportEntity getImportById(Integer id) {
        return importRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nhập có id=" + id));
    }
}

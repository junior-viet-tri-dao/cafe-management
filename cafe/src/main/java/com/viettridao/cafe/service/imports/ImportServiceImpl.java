package com.viettridao.cafe.service.imports;

import com.viettridao.cafe.model.ProductEntity;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.imports.ImportCreateRequest;
import com.viettridao.cafe.dto.request.imports.ImportUpdateRequest;
import com.viettridao.cafe.mapper.ImportMapper;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements IImportService {

    private final ImportRepository importRepository;
    private final ImportMapper importMapper;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void createImport(ImportCreateRequest request) {

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng nhập phải lớn hơn 0");
        }

        ImportEntity entity = importMapper.toEntity(request);

        entity.setEmployee(
                employeeRepository.findById(request.getEmployeeId())
                        .orElseThrow(() -> new RuntimeException("Employee not found"))
        );

        entity.setProduct(
                productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"))
        );

        ProductEntity product = productRepository.findProductById(entity.getProduct().getId());
        product.setQuantity(product.getQuantity() + entity.getQuantity());
        productRepository.save(product);

        importRepository.save(entity);
    }

    @Override
    public ImportUpdateRequest getUpdateForm(Integer id) {
        return importMapper.toUpdateRequest(findImportOrThrow(id));
    }

    @Override
    @Transactional
    public void updateImport(Integer id, ImportUpdateRequest request) {

        ImportEntity existing = findImportOrThrow(id);

        int oldQuantity = existing.getQuantity();
        int newQuantity = request.getQuantity();
        int delta = newQuantity - oldQuantity;

        importMapper.updateEntityFromRequest(request, existing);

        existing.setProduct(
                productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"))
        );

        ProductEntity product = productRepository.findProductById(request.getProductId());
        product.setQuantity(product.getQuantity() + delta);

        productRepository.save(product);

        importRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteImport(Integer id) {

        ImportEntity entity = findImportOrThrow(id);

        entity.setDeleted(true);

        ProductEntity product = productRepository.findProductById(entity.getProduct().getId());
        product.setQuantity(product.getQuantity() + entity.getQuantity());
        productRepository.save(product);

        importRepository.save(entity);
    }

    private ImportEntity findImportOrThrow(Integer id) {
        return importRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nhập với id = " + id));
    }
}

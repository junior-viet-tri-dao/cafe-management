package com.viettridao.cafe.service.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.product.ProductCreateRequest;
import com.viettridao.cafe.dto.request.product.ProductUpdateRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.repository.UnitRepository;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UnitRepository unitRepository;

    @Override
    public List<ProductResponse> getProductAll() {
        return productRepository.findProductByDeletedFalse()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getByProductById(Integer id) {

        ProductEntity entity = findProductOrThrow(id);

        return productMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {

        ProductEntity entity = productMapper.toEntity(request);
        // Gán đơn vị nếu request chứa unitId
        if (request.getUnitId() != null) {
            UnitEntity unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị"));
            entity.setUnit(unit);
        }
        ProductEntity savedEntity = productRepository.save(entity);
        return productMapper.toResponse(savedEntity);
    }

    @Override
    public ProductUpdateRequest getUpdateForm(Integer id) {
        return productMapper.toUpdateRequest(findProductOrThrow(id));
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, ProductUpdateRequest request) {

        ProductEntity existing = findProductOrThrow(id);

        productMapper.updateFromRequest(request,existing);

        if (request.getUnitId() != null) {
            UnitEntity unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị"));
            existing.setUnit(unit);
        }

        productRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {

        ProductEntity entity = findProductOrThrow(id);
        entity.setDeleted(true);
        productRepository.save(entity);
    }

    // Kiểm tra xem có hàng hóa có tồn tại theo id không
    private ProductEntity findProductOrThrow(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa với id = " + id));
    }
}

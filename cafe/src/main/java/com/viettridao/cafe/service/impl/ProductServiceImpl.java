package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.dto.request.product.UpdateProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponsePage;
import com.viettridao.cafe.dto.response.promotion.PromotionResponsePage;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.PromotionEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.viettridao.cafe.service.ProductService;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;



    @Override
    public ProductEntity getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin hàng hoá có Id =" + id));
    }

    @Override
    public ProductResponsePage getAllProductPage(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productEntities;
        if(StringUtils.hasText(keyword)){
            //productEntities = productRepository.getAllProductPageSearch(keyword, pageable);
            productEntities = productRepository.getAllProductPageSearch(keyword, pageable);
        }
        else {
            productEntities = productRepository.getAllProductPage(pageable);
        }

        ProductResponsePage productResponsePage = new ProductResponsePage();

        productResponsePage.setPageSize(productEntities.getSize());
        productResponsePage.setTotalPages(productEntities.getTotalPages());
        productResponsePage.setPageNumber(productEntities.getNumber());
        productResponsePage.setProductPage(productEntities.getContent());
        productResponsePage.setTotalElements(productEntities.getTotalElements());

        return productResponsePage;
    }

    @Override
    public ProductEntity updateProduct(UpdateProductRequest request) {
        ProductEntity productEntity = getProductById(request.getId());
        productEntity.setProductName(request.getProductName());


        //ImportEntity importEntity = importRepository.findById();

        return productRepository.save(productEntity);

    }

    @Override
    public void createProduct(CreateProductRequest request) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(request.getProductName());

        // minh co id unit id tim dc
        UnitEntity unitEntity = unitRepository.findById(request.getUnitId()).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị tính với Id =" + request.getUnitId()));
        productEntity.setUnit(unitEntity);
        productEntity.setIsDeleted(false);
        productEntity.setQuantity(0);
        productRepository.save(productEntity);
    }

    @Override
    public List<ProductEntity> getAllProduct() {
        return productRepository.findAll();
    }

    public ProductEntity getProductById(int Id){
        return productRepository.findById(Id).orElseThrow(() -> new RuntimeException("Không tìm được sản phẩm có Id =" + Id));
    }

}

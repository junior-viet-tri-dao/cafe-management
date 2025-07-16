package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.dto.request.menu.CreateMenuItemRequest;
import com.viettridao.cafe.dto.response.importProduct.ImportResponsePage;
import com.viettridao.cafe.dto.response.menu.MenuItemResponsePage;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {
    private final ImportRepository importRepository;
    private final ProductRepository productRepository;

    @Override
    public ImportEntity getImportbyId( Integer id ) {
        return importRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy đơn nhập có id =" + id));
    }

    @Override
    public void createImport(CreateImportRequest request) {




        ImportEntity importEntity = new ImportEntity();
        importEntity.setQuantity(request.getQuantity());
        importEntity.setIsDeleted(false);
        importEntity.setPrice(request.getPrice());
        importEntity.setImportDate(request.getImportDate());

        ProductEntity productEntity = productRepository.findById(request.getProduct_id()).orElseThrow(() -> new RuntimeException("Không tìm thấy product id =" + request.getProduct_id()));
        importEntity.setProduct(productEntity);
        importRepository.save(importEntity);
        // update price and quanlity of product
        // set gia moi va quanlity moi cho product
        productEntity.setQuantity(productEntity.getQuantity() + request.getQuantity());
        double priceNew = (productEntity.getQuantity() * productEntity.getPrice() + request.getQuantity() * request.getPrice()) / (productEntity.getQuantity() + request.getQuantity());
        productEntity.setPrice((float) priceNew);
        productRepository.save(productEntity);

    }

    @Override
    public void updateImport(UpdateImportRequest request) {
        ImportEntity importEntity = getImportbyId(request.getId());
        importEntity.setImportDate(request.getImportDate());
        importEntity.setQuantity(request.getQuantity());
        importEntity.setTotalAmount(request.getTotalAmount());
        importEntity.setPrice(request.getPrice());

        importRepository.save(importEntity);

    }

    @Override
    public void deleteImportbyId(Integer id) {
        ImportEntity importEntity = importRepository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy đơn nhập với id = " + id));
        importEntity.setIsDeleted(true);
        importRepository.save(importEntity);
    }

    @Override
    public ImportResponsePage getAllImportPage(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImportEntity> importEntities;

        if(StringUtils.hasText(keyword)){
            importEntities = importRepository.getAllImportPageSearch(keyword, pageable);
        }
        else{
            importEntities = importRepository.getAllImportPage(pageable);
        }


        ImportResponsePage importResponsePage = new ImportResponsePage();
        importResponsePage.setPageSize(importEntities.getSize());
        importResponsePage.setPageNumber(importEntities.getNumber());
        importResponsePage.setTotalPages(importEntities.getTotalPages());
        importResponsePage.setTotalElements(importEntities.getTotalElements());
        importResponsePage.setImportPage(importEntities.getContent());


        return importResponsePage;
    }


}

package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.export.UpdateExportRequest;
import com.viettridao.cafe.dto.response.export.ExportResponsePage;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {
    private final ExportRepository exportRepository;
    private final ProductRepository productRepository;


    @Override
    public ExportEntity getExportbyId(Integer id) {
        return exportRepository.findById(id).orElseThrow(() -> new RuntimeException("Không thể tìm dược đơn xuất =" + id));
    }

    @Override
    public void createExport(CreateExportRequest request) {
        ExportEntity exportEntity = new ExportEntity();
        exportEntity.setQuantity(request.getQuantity());
        exportEntity.setExportDate(request.getExportDate());

        ProductEntity productEntity = productRepository.findById(request.getProduct_id()).orElseThrow(()->new RuntimeException("Không thể tìm được hàng hoá có id =" + request.getProduct_id()));
        if(productEntity.getQuantity()  >= request.getQuantity()){
            exportEntity.setProduct(productEntity);
            exportRepository.save(exportEntity);

            productEntity.setQuantity(productEntity.getQuantity() - request.getQuantity());
            productRepository.save(productEntity);
        }


    }

    @Override
    public void deleteExportbyId(Integer id) {
        ExportEntity exportEntity = getExportbyId(id);
        exportEntity.setIsDeleted(true);
        exportRepository.save(exportEntity);
    }

    @Override
    public void updateExport(UpdateExportRequest request) {
        ExportEntity exportEntity = getExportbyId(request.getId());
        exportEntity.setQuantity(request.getQuantity());
        exportEntity.setExportDate(request.getExportDate());
        exportRepository.save(exportEntity);
    }

    @Override
    public ExportResponsePage getAllIExportPage(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExportEntity> exportEntities;

        if(StringUtils.hasText(keyword)){
            exportEntities= exportRepository.getAllExportPageSearch(keyword,pageable);
        }
        else{
            exportEntities = exportRepository.getAllExportPage(pageable);

        }

        ExportResponsePage exportResponsePage = new ExportResponsePage();
        exportResponsePage.setExportPage(exportEntities.getContent());
        exportResponsePage.setPageNumber(exportEntities.getNumber());
        exportResponsePage.setPageSize(exportEntities.getSize());
        exportResponsePage.setTotalPages(exportEntities.getTotalPages());
        exportResponsePage.setTotalElements(exportEntities.getTotalElements());

        return exportResponsePage;

    }
}

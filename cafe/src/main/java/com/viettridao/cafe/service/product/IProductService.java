package com.viettridao.cafe.service.product;

import java.util.List;

import com.viettridao.cafe.dto.request.product.ProductCreateRequest;
import com.viettridao.cafe.dto.request.product.ProductUpdateRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;

public interface IProductService {

    List<ProductResponse> getProductAll();

    ProductResponse getByProductById(Integer id);

    ProductResponse createProduct(ProductCreateRequest request);

    ProductUpdateRequest getUpdateForm(Integer id);

    void updateProduct(Integer id, ProductUpdateRequest request);

    void deleteProduct(Integer id);
}

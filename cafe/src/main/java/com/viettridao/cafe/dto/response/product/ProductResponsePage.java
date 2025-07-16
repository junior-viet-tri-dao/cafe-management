package com.viettridao.cafe.dto.response.product;

import com.viettridao.cafe.dto.response.PageResponse;
import com.viettridao.cafe.model.ProductEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponsePage extends PageResponse {

    private List<ProductEntity> productPage;
}

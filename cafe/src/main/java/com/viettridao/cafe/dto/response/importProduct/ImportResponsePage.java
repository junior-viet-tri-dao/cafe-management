package com.viettridao.cafe.dto.response.importProduct;

import com.viettridao.cafe.dto.response.PageResponse;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.PromotionEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImportResponsePage extends PageResponse {

    private List<ImportEntity> importPage;
}

package com.viettridao.cafe.dto.response.export;

import com.viettridao.cafe.dto.response.PageResponse;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExportResponsePage extends PageResponse {

    private List<ExportEntity> exportPage;
}

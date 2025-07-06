package com.viettridao.cafe.dto.response.table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableResponse {

    private Integer id;

    private String status;

    private String tableName;

    private Boolean deleted;

}

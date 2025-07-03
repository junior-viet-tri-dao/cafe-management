package com.viettridao.cafe.dto.response.table;

import com.viettridao.cafe.common.TableStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableResponse {
    private Integer id;

    private String status;

    private String tableName;

    private Boolean isDeleted;
}

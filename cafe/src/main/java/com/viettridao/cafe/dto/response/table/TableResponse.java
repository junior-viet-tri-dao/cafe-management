package com.viettridao.cafe.dto.response.table;

import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Getter
@Setter
public class TableResponse {

    private Integer id;

    private String status;

    private String tableName;

    private Boolean deleted;

}

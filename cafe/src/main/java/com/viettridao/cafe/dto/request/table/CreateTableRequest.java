package com.viettridao.cafe.dto.request.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTableRequest {
    @NotBlank(message = "Tên bàn không được để trống")
    private String tableName;
}

package com.viettridao.cafe.dto.request.table;

import com.viettridao.cafe.common.TableStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableRequest {
    @NotBlank(message = "Tên bàn không được để trống")
    private String tableName;
}

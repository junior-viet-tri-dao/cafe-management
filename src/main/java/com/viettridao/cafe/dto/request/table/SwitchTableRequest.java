package com.viettridao.cafe.dto.request.table;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwitchTableRequest {
    @NotNull(message = "Vui lòng chọn bàn chuyển đi")
    private Integer fromTableId;

    @NotNull(message = "Vui lòng chọn bàn chuyển đến")
    private Integer toTableId;
}
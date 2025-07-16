package com.viettridao.cafe.dto.request.table;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SplitItemRequest {
    private Integer itemId;

    private Integer quantity;
}

package com.viettridao.cafe.dto.request.unit;

import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Getter
@Setter
public class UnitRequest {

    private String unitName;

    private Boolean deleted;
}

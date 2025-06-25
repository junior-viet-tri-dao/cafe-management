package com.viettridao.cafe.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ImportResponse {
    private Integer id;

    private LocalDate importDate;
}

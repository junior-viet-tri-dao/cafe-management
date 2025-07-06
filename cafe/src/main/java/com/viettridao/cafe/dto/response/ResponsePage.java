package com.viettridao.cafe.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResponsePage {
    private long pageNumber;

    private long pageSize;

    private long totalPages;

    private long totalElements;
}

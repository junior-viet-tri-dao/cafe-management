package com.viettridao.cafe.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    private Integer maThucDon;

    private String tenMon;

    private Double giaTien;
}

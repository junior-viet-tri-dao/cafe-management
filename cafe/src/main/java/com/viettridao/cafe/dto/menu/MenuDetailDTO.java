package com.viettridao.cafe.dto.menu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuDetailDTO {
    private String tenMon;

    private Double giaTien;

    private List<ElementDTO> thanhPhanList;
}

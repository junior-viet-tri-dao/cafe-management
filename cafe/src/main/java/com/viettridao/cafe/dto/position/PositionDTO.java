package com.viettridao.cafe.dto.position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionDTO {
    private Integer maChucVu;

    private String tenChucVu;

    private Double luong;
}

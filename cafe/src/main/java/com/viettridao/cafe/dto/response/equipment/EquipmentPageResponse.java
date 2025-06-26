package com.viettridao.cafe.dto.response.equipment;

import com.viettridao.cafe.dto.response.PageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EquipmentPageResponse extends PageResponse {
    private List<EquipmentResponse> equipments;
}

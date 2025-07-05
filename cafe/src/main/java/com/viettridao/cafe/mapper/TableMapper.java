package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.table.TableCreateRequest;
import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.model.TableEntity;

@Mapper(componentModel = "spring")
public interface TableMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "reservations", ignore = true)
    TableEntity toEntity(TableCreateRequest request);

    TableResponse toResponse(TableEntity entity);
}

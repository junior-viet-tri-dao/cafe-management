package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.viettridao.cafe.dto.response.tables.TableResponse;
import com.viettridao.cafe.model.TableEntity;

@Mapper(componentModel = "spring")
public interface TableMapper {

	TableResponse toDto(TableEntity entity);

	List<TableResponse> toDtoList(List<TableEntity> entities);
}

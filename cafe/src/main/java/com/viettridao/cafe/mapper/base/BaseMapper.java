package com.viettridao.cafe.mapper.base;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

public abstract class BaseMapper<T, Req, Res> {

	protected final ModelMapper modelMapper;
	private final Class<T> entityClass;
	private final Class<Req> requestClass;
	private final Class<Res> responseClass;

	protected BaseMapper(ModelMapper modelMapper, Class<T> entityClass, Class<Req> requestClass,
			Class<Res> responseClass) {
		this.modelMapper = modelMapper;
		this.entityClass = entityClass;
		this.requestClass = requestClass;
		this.responseClass = responseClass;
	}

	public T fromRequest(Req dto) {
		return modelMapper.map(dto, entityClass);
	}

	public Res toDto(T entity) {
		return modelMapper.map(entity, responseClass);
	}

	public List<Res> toDtoList(List<T> entities) {
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}

	public List<T> fromRequestList(List<Req> dtoList) {
		return dtoList.stream().map(this::fromRequest).collect(Collectors.toList());
	}

	public void updateEntityFromRequest(Req dto, T entity) {
		modelMapper.map(dto, entity);
	}
}

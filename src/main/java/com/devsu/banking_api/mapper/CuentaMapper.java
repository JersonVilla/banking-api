package com.devsu.banking_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsu.banking_api.dto.CuentaDTO;
import com.devsu.banking_api.model.entity.Cuenta;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

	@Mapping(source = "cliente.id", target = "clienteId")
	CuentaDTO toDTO(Cuenta cuenta);
}

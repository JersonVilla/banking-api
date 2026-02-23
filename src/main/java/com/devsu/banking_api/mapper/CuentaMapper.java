package com.devsu.banking_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsu.banking_api.dto.CuentaDTO;
import com.devsu.banking_api.dto.CuentaResponseDTO;
import com.devsu.banking_api.model.entity.Cuenta;

@Mapper(componentModel = "spring", uses = {MovimientoMapper.class})
public interface CuentaMapper {

	@Mapping(source = "cliente.id", target = "clienteId")
	CuentaDTO toDTO(Cuenta cuenta);
	
	@Mapping(source = "clienteId", target = "cliente.id")
	@Mapping(target = "movimientos", ignore = true)
	Cuenta toEntity(CuentaDTO dto);
	
	@Mapping(source = "cliente.nombre", target = "nombreCliente")
	CuentaResponseDTO toResponseDTO(Cuenta cuenta);
}

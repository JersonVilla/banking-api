package com.devsu.banking_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsu.banking_api.dto.MovimientoResponseDTO;
import com.devsu.banking_api.model.entity.Movimiento;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

	@Mapping(target = "cuenta", ignore = true)
	Movimiento toEntity(MovimientoResponseDTO dto);
	
	@Mapping(target = "numeroCuenta", ignore = true)
	MovimientoResponseDTO toDTO(Movimiento movimiento);
	
}

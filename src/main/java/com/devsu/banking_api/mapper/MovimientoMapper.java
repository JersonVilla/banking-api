package com.devsu.banking_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsu.banking_api.dto.MovimientoResponseDTO;
import com.devsu.banking_api.model.entity.Movimiento;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {
	
	@Mapping(target = "cuenta", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "saldo", source = "saldoDisponible")
	@Mapping(target = "valor", source = "movimiento")
	Movimiento toEntity(MovimientoResponseDTO dto);
	
	@Mapping(source = "cuenta.numeroCuenta", target = "numeroCuenta")
    @Mapping(source = "cuenta.tipoCuenta", target = "tipoCuenta")
    @Mapping(source = "cuenta.saldoInicial", target = "saldoInicial")
    @Mapping(source = "cuenta.estado", target = "estado")
    @Mapping(source = "cuenta.cliente.nombre", target = "cliente")
    @Mapping(source = "valor", target = "movimiento")
    @Mapping(source = "saldo", target = "saldoDisponible")
	MovimientoResponseDTO toDTO(Movimiento movimiento);
	
}

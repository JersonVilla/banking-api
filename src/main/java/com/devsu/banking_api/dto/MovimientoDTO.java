package com.devsu.banking_api.dto;

import lombok.Data;

@Data
public class MovimientoDTO {

	private Long movimientoId;
	private String numeroCuenta;
	private String tipoMovimiento;
	private Double valor;
	
}

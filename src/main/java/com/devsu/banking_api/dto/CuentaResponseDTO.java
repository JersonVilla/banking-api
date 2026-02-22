package com.devsu.banking_api.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CuentaResponseDTO {

	private String numeroCuenta;
	private String tipoCuenta;
	private BigDecimal saldoInicial;
	private BigDecimal saldofinal;
	private Boolean estado;
	private String nombreCliente;
	private List<MovimientoDTO> movimientos;
	
}

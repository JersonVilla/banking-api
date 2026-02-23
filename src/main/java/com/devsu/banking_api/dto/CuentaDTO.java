package com.devsu.banking_api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CuentaDTO {

	private String numeroCuenta;
	private String tipoCuenta;
	private BigDecimal saldoInicial;
	private Boolean estado;
	private Long clienteId;
	
}

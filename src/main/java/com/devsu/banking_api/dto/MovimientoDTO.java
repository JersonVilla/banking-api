package com.devsu.banking_api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {

	private String numeroCuenta;
    private String tipoMovimiento;
    private BigDecimal valor;
    
}

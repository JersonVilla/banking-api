package com.devsu.banking_api.service;

import java.util.List;

import com.devsu.banking_api.dto.CuentaDTO;
import com.devsu.banking_api.dto.CuentaResponseDTO;

public interface ICuentaService {

	CuentaResponseDTO crear(CuentaDTO dto);
	
	CuentaResponseDTO actualizar(String numeroCuenta, CuentaDTO dto);
	
	void eliminar(String numeroCuenta);
	
	List<CuentaResponseDTO> listar();
	
	CuentaResponseDTO obtenerPorNumeroCuenta(String numeroCuenta);
	
}

package com.devsu.banking_api.service;

import java.util.List;

import com.devsu.banking_api.dto.CuentaDTO;
import com.devsu.banking_api.dto.CuentaResponseDTO;

public interface ICuentaService {

	/**
	 * Crear cuenta
	 * 
	 * @param dto
	 * @return CuentaResponseDTO
	 */
	CuentaResponseDTO crear(CuentaDTO dto);
	
	/**
	 * Actualizar cuenta
	 * 
	 * @param numeroCuenta
	 * @param dto
	 * @return CuentaResponseDTO
	 */
	CuentaResponseDTO actualizar(String numeroCuenta, CuentaDTO dto);
	
	/**
	 * Eliminar cuenta
	 * 
	 * @param numeroCuenta
	 */
	void eliminar(String numeroCuenta);
	
	/**
	 * Listar cuentas
	 * 
	 * @return List
	 */
	List<CuentaResponseDTO> listar();
	
	/**
	 * Obtener cuenta por numero
	 * @param numeroCuenta
	 * @return CuentaResponseDTO
	 */
	CuentaResponseDTO obtenerPorNumeroCuenta(String numeroCuenta);
	
	/**
	 * Listar cuentas activas
	 * 
	 * @return List
	 */
	List<CuentaDTO> getCuentasActivas();
}

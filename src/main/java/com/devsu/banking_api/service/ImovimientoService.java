package com.devsu.banking_api.service;

import java.time.LocalDateTime;
import java.util.List;

import com.devsu.banking_api.dto.MovimientoDTO;
import com.devsu.banking_api.dto.MovimientoResponseDTO;

public interface ImovimientoService {

	/**
	 * Crear movimiento
	 * 
	 * @param movimiento
	 * @return MovimientoResponseDTO
	 */
	MovimientoResponseDTO crear(MovimientoDTO movimiento);
	
	/**
	 * Listar clientes
	 * 
	 * @return List
	 */
	List<MovimientoResponseDTO> listar();
	
	/**
	 * Listar movimientos
	 * 
	 * @param cuenta
	 * @return List
	 */
	List<MovimientoResponseDTO> listarMovimientosPorCuenta(String cuenta);
	
	/**
	 * Generar reporte de movimientos
	 * 
	 * @param cuenta
	 * @param inicio
	 * @param fin
	 * @return List
	 */
	List<MovimientoResponseDTO> listarMovimientosPorCuentaYFecha(String cuenta, LocalDateTime inicio, LocalDateTime fin);
	
}

package com.devsu.banking_api.service;

import java.time.LocalDateTime;
import java.util.List;

import com.devsu.banking_api.dto.MovimientoDTO;
import com.devsu.banking_api.dto.MovimientoResponseDTO;

public interface ImovimientoService {

	MovimientoResponseDTO crear(MovimientoDTO movimiento);
	
	List<MovimientoResponseDTO> listarMovimientosPorCuenta(String cuenta);
	
	List<MovimientoResponseDTO> listarMovimientosPorCuentaYFecha(String cuenta, LocalDateTime inicio, LocalDateTime fin);
	
}

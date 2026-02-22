package com.devsu.banking_api.service;

import java.time.LocalDateTime;
import java.util.List;

import com.devsu.banking_api.dto.MovimientoDTO;

public interface ImovimientoService {

	MovimientoDTO crear(MovimientoDTO movimiento);
	
	List<MovimientoDTO> listarMovimientosPorCuenta(String cuenta);
	
	List<MovimientoDTO> listarMovimientosPorCuentaYFecha(String cuenta, LocalDateTime inicio, LocalDateTime fin);
	
}

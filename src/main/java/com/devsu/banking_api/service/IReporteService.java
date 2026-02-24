package com.devsu.banking_api.service;

import java.util.List;

import com.devsu.banking_api.dto.MovimientoResponseDTO;

public interface IReporteService {

	/**
     * Genera un PDF con la lista de movimientos
     * @param movimientos lista de movimientos
     * @return arreglo de bytes del PDF
     */
    byte[] generarReporteMovimientos(List<MovimientoResponseDTO> movimientos);
    
}

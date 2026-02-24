package com.devsu.banking_api.service;

import java.util.List;

import com.devsu.banking_api.dto.ClienteDTO;
import com.devsu.banking_api.dto.ClienteResponseDTO;

public interface IClienteService {

	/**
	 * Clear clientes
	 * 
	 * @param dto
	 * @return ClienteResponseDTO
	 */
	ClienteResponseDTO crear(ClienteDTO dto);
	
	/**
	 * Actualizar clientes
	 * 
	 * @param id
	 * @param dto
	 * @return ClienteResponseDTO
	 */
	ClienteResponseDTO actualizar(Long id, ClienteDTO dto);
	
	/**
	 * Eliminar clientes
	 * 
	 * @param id
	 */
	void eliminar(Long id);
	
	/**
	 * Listar clientes
	 * 
	 * @return List
	 */
	List<ClienteResponseDTO> listar();
	
	/**
	 * Obtener clientes por id
	 * 
	 * @param id
	 * @return ClienteResponseDTO
	 */
	ClienteResponseDTO obtenerPorId(Long id);
	
}

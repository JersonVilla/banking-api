package com.devsu.banking_api.service;

import java.util.List;

import com.devsu.banking_api.dto.ClienteDTO;
import com.devsu.banking_api.dto.ClienteResponseDTO;

public interface IClienteService {

	ClienteResponseDTO crear(ClienteDTO dto);
	
	ClienteResponseDTO actualizar(Long id, ClienteDTO dto);
	
	void eliminar(Long id);
	
	List<ClienteResponseDTO> listar();
	
	ClienteResponseDTO obtenerPorId(Long id);
	
}

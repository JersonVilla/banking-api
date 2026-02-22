package com.devsu.banking_api.service;

import java.util.List;

import com.devsu.banking_api.dto.ClienteDTO;

public interface IClienteService {

	ClienteDTO crear(ClienteDTO dto);
	
	ClienteDTO actualizar(Long id, ClienteDTO dto);
	
	void eliminar(Long id);
	
	List<ClienteDTO> listar();
	
	ClienteDTO obtenerPorId(Long id);
	
}

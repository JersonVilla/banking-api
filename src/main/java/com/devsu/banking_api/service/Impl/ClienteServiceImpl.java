package com.devsu.banking_api.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.ClienteDTO;
import com.devsu.banking_api.exception.ResourceNotFoundException;
import com.devsu.banking_api.mapper.ClienteMapper;
import com.devsu.banking_api.model.entity.Cliente;
import com.devsu.banking_api.model.repository.ClienteRepository;
import com.devsu.banking_api.service.IClienteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClienteServiceImpl implements IClienteService {
	
	private final ClienteRepository clienteRepository;
	private final ClienteMapper mapper;
	
	public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper mapper) {
		this.clienteRepository = clienteRepository;
		this.mapper = mapper;
	}

	@Override
	public ClienteDTO crear(ClienteDTO dto) {
		Cliente cliente= mapper.toEntity(dto);
		cliente.setEstado(true);
		
		return mapper.toDTO(clienteRepository.save(cliente));
	}

	@Override
	public ClienteDTO actualizar(Long id, ClienteDTO dto) {
		Cliente cliente =  clienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El cliente no se encuentra registrado en la BD"));
		
		cliente.setNombre(dto.getNombre());
		cliente.setDireccion(dto.getDireccion());
		
		return mapper.toDTO(clienteRepository.save(cliente));
	}

	@Override
	public void eliminar(Long id) {
		clienteRepository.deleteById(id);
	}

	@Override
	public List<ClienteDTO> listar() {
		log.info("Listar clientes");
		return clienteRepository.findAll()
				.stream()
				.map(mapper::toDTO)
				.toList();
	}

	@Override
	public ClienteDTO obtenerPorId(Long id) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("El cliente no se encuentra registrado en la BD"));
		
		return mapper.toDTO(cliente);
	}

	

}

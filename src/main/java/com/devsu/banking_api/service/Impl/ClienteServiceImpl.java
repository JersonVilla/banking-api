package com.devsu.banking_api.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.ClienteDTO;
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
		log.info("Creando cliente con identificación: {}", dto.getIdentificacion());

        if (clienteRepository.existsByIdentificacion(dto.getIdentificacion())) {
            log.error("El cliente con identificación {} ya existe", dto.getIdentificacion());
            throw new RuntimeException("Cliente ya registrado");
        }

        Cliente cliente = mapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente creado con id: {}", guardado.getId());
        return mapper.toDTO(guardado);
	}

	@Override
	public ClienteDTO actualizar(Long id, ClienteDTO dto) {
		log.info("Actualizando cliente con id: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente con id {} no encontrado", id);
                    return new RuntimeException("Cliente no encontrado");
                });

        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad().toString());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setContrasena(dto.getContrasena());
        cliente.setEstado(dto.getEstado());

        Cliente actualizado = clienteRepository.save(cliente);
        log.info("Cliente actualizado con id: {}", actualizado.getId());
        return mapper.toDTO(actualizado);
	}

	@Override
	public void eliminar(Long id) {
		log.info("Eliminando cliente con id: {}", id);
        if (!clienteRepository.existsById(id)) {
            log.error("Cliente con id {} no encontrado", id);
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
        log.info("Cliente eliminado con id: {}", id);
	}

	@Override
	public List<ClienteDTO> listar() {
		log.info("Listando todos los clientes");
        return clienteRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
	}

	@Override
	public ClienteDTO obtenerPorId(Long id) {
		log.info("Obteniendo cliente por id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente con id {} no encontrado", id);
                    return new RuntimeException("Cliente no encontrado");
                });
        return mapper.toDTO(cliente);
	}

	

}

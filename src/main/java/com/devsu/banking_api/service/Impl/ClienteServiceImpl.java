package com.devsu.banking_api.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.ClienteDTO;
import com.devsu.banking_api.dto.ClienteResponseDTO;
import com.devsu.banking_api.exception.BadRequestException;
import com.devsu.banking_api.exception.NotFoundException;
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
	
	private static final String CLIENTE_NO_ENCONTRADO = "Cliente no encontrado";
	private static final String CLIENTE_EXISTE = "El cliente ya se encuentra registrado";
	
	public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper mapper) {
		this.clienteRepository = clienteRepository;
		this.mapper = mapper;
	}

	@Override
	public ClienteResponseDTO crear(ClienteDTO dto) {
		log.info("Creando cliente con identificación: {}", dto.getIdentificacion());

        if (clienteRepository.existsByIdentificacion(dto.getIdentificacion())) {
            log.error("El cliente con identificación {} ya existe", dto.getIdentificacion());
            throw new BadRequestException(CLIENTE_EXISTE);
        }

        Cliente cliente = mapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente creado con id: {}", guardado.getId());
        return mapper.toResponseDTO(guardado);
	}

	@Override
	public ClienteResponseDTO actualizar(Long id, ClienteDTO dto) {
		log.info("Actualizando cliente con id: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente con id {} no encontrado", id);
                    return new NotFoundException(CLIENTE_NO_ENCONTRADO);
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
        return mapper.toResponseDTO(actualizado);
	}

	@Override
	public void eliminar(Long id) {
		log.info("Eliminando cliente con id: {}", id);
        if (!clienteRepository.existsById(id)) {
            log.error("Cliente con id {} no encontrado", id);
            throw new NotFoundException(CLIENTE_NO_ENCONTRADO);
        }
        clienteRepository.deleteById(id);
        log.info("Cliente eliminado con id: {}", id);
	}

	@Override
	public List<ClienteResponseDTO> listar() {
		log.info("Listando todos los clientes");
        return clienteRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
	}

	@Override
	public ClienteResponseDTO obtenerPorId(Long id) {
		log.info("Obteniendo cliente por id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente con id {} no encontrado", id);
                    return new NotFoundException(CLIENTE_NO_ENCONTRADO);
                });
        return mapper.toResponseDTO(cliente);
	}

	

}

package com.devsu.banking_api.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.ClienteBasicDTO;
import com.devsu.banking_api.dto.ClienteDTO;
import com.devsu.banking_api.dto.ClienteResponseDTO;
import com.devsu.banking_api.exception.BadRequestException;
import com.devsu.banking_api.exception.NotFoundException;
import com.devsu.banking_api.mapper.ClienteMapper;
import com.devsu.banking_api.model.entity.Cliente;
import com.devsu.banking_api.model.repository.ClienteRepository;
import com.devsu.banking_api.service.IClienteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements IClienteService {
	
	private final ClienteRepository clienteRepository;
	private final ClienteMapper mapper;
	
	private static final String CLIENTE_NO_ENCONTRADO = "Cliente no encontrado";
	private static final String CLIENTE_EXISTE = "El cliente ya se encuentra registrado";

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

        actualizarCampos(cliente, dto);

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

	@Override
	public List<ClienteBasicDTO> listarActivos() {
		log.info("Consultando clientes activos");

	    List<Object[]> resultados = clienteRepository.findClientesActivosNative();

	    List<ClienteBasicDTO> clientes = resultados.stream()
	            .map(obj -> new ClienteBasicDTO(
	                    ((Number) obj[0]).longValue(),
	                    (String) obj[1]
	            ))
	            .toList();

	    log.info("Se encontraron {} clientes activos", clientes.size());

	    return clientes;
	}
	
	private void actualizarCampos(Cliente cliente, ClienteDTO dto) {
        Optional.ofNullable(dto.getNombre()).filter(s -> !s.isBlank()).ifPresent(cliente::setNombre);
        Optional.ofNullable(dto.getGenero()).filter(s -> !s.isBlank()).ifPresent(cliente::setGenero);
        Optional.ofNullable(dto.getEdad()).ifPresent(edad -> cliente.setEdad(edad.toString()));
        Optional.ofNullable(dto.getDireccion()).filter(s -> !s.isBlank()).ifPresent(cliente::setDireccion);
        Optional.ofNullable(dto.getTelefono()).filter(s -> !s.isBlank()).ifPresent(cliente::setTelefono);
        Optional.ofNullable(dto.getContrasena()).filter(s -> !s.isBlank()).ifPresent(cliente::setContrasena);
        Optional.ofNullable(dto.getEstado()).ifPresent(cliente::setEstado);
    }

}

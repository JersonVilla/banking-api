package com.devsu.banking_api.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.CuentaDTO;
import com.devsu.banking_api.dto.CuentaResponseDTO;
import com.devsu.banking_api.exception.BadRequestException;
import com.devsu.banking_api.exception.NotFoundException;
import com.devsu.banking_api.mapper.CuentaMapper;
import com.devsu.banking_api.model.entity.Cliente;
import com.devsu.banking_api.model.entity.Cuenta;
import com.devsu.banking_api.model.repository.ClienteRepository;
import com.devsu.banking_api.model.repository.CuentaRepository;
import com.devsu.banking_api.service.ICuentaService;

@Service
public class CuentaServiceImpl implements ICuentaService {

	private final CuentaRepository cuentaRepository;
	private final ClienteRepository clienteRepository;
	private final CuentaMapper mapper;
	
	private static final String CUENTA_NO_ENCONTRADA = "Cuenta no encontrada";
	private static final String CLIENTE_NO_ENCONTRADO = "Cliente no encontrado";
	private static final String CUENTA_EXISTE = "La cuenta ya existe";
	
	public CuentaServiceImpl(CuentaRepository cuentaRepository, ClienteRepository clienteRepository,
			CuentaMapper mapper) {
		this.cuentaRepository = cuentaRepository;
		this.clienteRepository = clienteRepository;
		this.mapper = mapper;
	}

	@Override
	public CuentaResponseDTO crear(CuentaDTO dto) {
		Cliente cliente = obtenerCliente(dto.getClienteId());
		
		if(cuentaRepository.existsById(dto.getNumeroCuenta())) {
			new BadRequestException(CUENTA_EXISTE);
		}
		
		Cuenta cuenta = mapper.toEntity(dto);
		cuenta.setCliente(cliente);
		cuenta.setMovimientos(new ArrayList<>());
		Cuenta guardarCuenta = cuentaRepository.save(cuenta);
		
		return mapper.toResponseDTO(guardarCuenta);
	}

	@Override
	public CuentaResponseDTO actualizar(String numeroCuenta, CuentaDTO dto) {
		Cuenta cuenta = obtenerCuenta(numeroCuenta);
		
		cuenta.setTipoCuenta(dto.getTipoCuenta());
		cuenta.setSaldoInicial(dto.getSaldoInicial());
		cuenta.setEstado(dto.getEstado());
		
		Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
		return mapper.toResponseDTO(cuentaActualizada);
	}

	@Override
	public void eliminar(String numeroCuenta) {
		obtenerCuenta(numeroCuenta);
		cuentaRepository.deleteById(numeroCuenta);
	}

	@Override
	public List<CuentaResponseDTO> listar() {
		return cuentaRepository.findAll().stream()
				.map(mapper::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Override
	public CuentaResponseDTO obtenerPorNumeroCuenta(String numeroCuenta) {
		Cuenta cuenta = obtenerCuenta(numeroCuenta);
		return mapper.toResponseDTO(cuenta);
	}
	
	private Cliente obtenerCliente(Long idCliente) {
        return clienteRepository.findById(idCliente)
        		.orElseThrow(() -> new NotFoundException(CLIENTE_NO_ENCONTRADO));
    }
	
	private Cuenta obtenerCuenta(String numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new NotFoundException(CUENTA_NO_ENCONTRADA));
    }

}

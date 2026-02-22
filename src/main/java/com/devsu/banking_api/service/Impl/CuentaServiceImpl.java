package com.devsu.banking_api.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.CuentaDTO;
import com.devsu.banking_api.dto.CuentaResponseDTO;
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
	
	public CuentaServiceImpl(CuentaRepository cuentaRepository, ClienteRepository clienteRepository,
			CuentaMapper mapper) {
		this.cuentaRepository = cuentaRepository;
		this.clienteRepository = clienteRepository;
		this.mapper = mapper;
	}

	@Override
	public CuentaResponseDTO crear(CuentaDTO dto) {
		Cliente cliente = clienteRepository.findById(dto.getClienteId())
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
		
		if(cuentaRepository.existsById(dto.getNumeroCuenta())) {
			throw new RuntimeException("La cuenta ya existe");
		}
		
		Cuenta cuenta = mapper.toEntity(dto);
		cuenta.setCliente(cliente);
		cuenta.setMovimientos(new ArrayList<>());
		Cuenta guardarCuenta = cuentaRepository.save(cuenta);
		
		return mapper.toResponseDTO(guardarCuenta);
	}

	@Override
	public CuentaResponseDTO actualizar(String numeroCuenta, CuentaDTO dto) {
		Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
				.orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
		
		cuenta.setTipoCuenta(dto.getTipoCuenta());
		cuenta.setSaldoInicial(dto.getSaldoInicial());
		cuenta.setEstado(dto.getEstado());
		
		Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
		return mapper.toResponseDTO(cuentaActualizada);
	}

	@Override
	public void eliminar(String numeroCuenta) {
		if(!cuentaRepository.existsById(numeroCuenta)) {
			throw new RuntimeException("Cuenta no encontrada");
		}
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
		Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
				.orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
		return mapper.toResponseDTO(cuenta);
	}

}

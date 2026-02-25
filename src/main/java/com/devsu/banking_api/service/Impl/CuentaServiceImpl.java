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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements ICuentaService {

	private final CuentaRepository cuentaRepository;
	private final ClienteRepository clienteRepository;
	private final CuentaMapper mapper;
	
	private static final String CUENTA_NO_ENCONTRADA = "Cuenta no encontrada";
	private static final String CUENTA_CON_MOVIMIENTOS = "No se puede eliminar una cuenta con movimientos asociados";
	private static final String CLIENTE_NO_ENCONTRADO = "Cliente no encontrado";
	private static final String CUENTA_EXISTE = "La cuenta ya existe";

	@Override
	public CuentaResponseDTO crear(CuentaDTO dto) {
		log.info("Creando cuenta con número: {}", dto.getNumeroCuenta());
		Cliente cliente = obtenerCliente(dto.getClienteId());
		
		if(cuentaRepository.existsById(dto.getNumeroCuenta())) {
			log.warn("Intento de crear cuenta existente: {}", dto.getNumeroCuenta());
			throw new BadRequestException(CUENTA_EXISTE);
		}
		
		Cuenta cuenta = mapper.toEntity(dto);
		cuenta.setCliente(cliente);
		cuenta.setMovimientos(new ArrayList<>());
		Cuenta guardarCuenta = cuentaRepository.save(cuenta);
		log.info("Cuenta creada con número: {}", guardarCuenta.getNumeroCuenta());
		return mapper.toResponseDTO(guardarCuenta);
	}

	@Override
	public CuentaResponseDTO actualizar(String numeroCuenta, CuentaDTO dto) {
		log.info("Actualizando cuenta: {}", numeroCuenta);
		Cuenta cuenta = obtenerCuenta(numeroCuenta);
		
		cuenta.setTipoCuenta(dto.getTipoCuenta());
		cuenta.setSaldoInicial(dto.getSaldoInicial());
		cuenta.setEstado(dto.getEstado());
		
		Cuenta actualizarCuenta = cuentaRepository.save(cuenta);
		log.info("Cuenta actualizada: {}", actualizarCuenta.getNumeroCuenta());
		return mapper.toResponseDTO(actualizarCuenta);
	}

	@Override
	public void eliminar(String numeroCuenta) {
		log.info("Eliminando cuenta: {}", numeroCuenta);
		Cuenta cuenta = obtenerCuenta(numeroCuenta);

	    if (cuenta.getMovimientos() != null && !cuenta.getMovimientos().isEmpty()) {
	    	log.warn("No se puede eliminar cuenta con movimientos: {}", numeroCuenta);
	        throw new BadRequestException(CUENTA_CON_MOVIMIENTOS);
	    }

	    cuentaRepository.delete(cuenta);
	    log.info("Cuenta eliminada: {}", numeroCuenta);
	}

	@Override
	public List<CuentaResponseDTO> listar() {
		List<Cuenta> cuentas = cuentaRepository.findAll();
        log.info("Listando {} cuentas", cuentas.size());
        return cuentas.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
	}

	@Override
	public CuentaResponseDTO obtenerPorNumeroCuenta(String numeroCuenta) {
		Cuenta cuenta = obtenerCuenta(numeroCuenta);
		return mapper.toResponseDTO(cuenta);
	}
	
	@Override
	public List<CuentaDTO> getCuentasActivas() {
		List<Cuenta> cuentasActivas = cuentaRepository.findByEstadoTrue();
        log.info("Cuentas activas encontradas: {}", cuentasActivas.size());
        return cuentasActivas.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
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

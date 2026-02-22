package com.devsu.banking_api.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.MovimientoDTO;
import com.devsu.banking_api.mapper.MovimientoMapper;
import com.devsu.banking_api.model.entity.Cuenta;
import com.devsu.banking_api.model.entity.Movimiento;
import com.devsu.banking_api.model.repository.CuentaRepository;
import com.devsu.banking_api.model.repository.MovimientoRepository;
import com.devsu.banking_api.service.ImovimientoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovimientoServiceImpl implements ImovimientoService {

	private final MovimientoRepository movimientoRepository;
	private final CuentaRepository cuentaRepository;
	private final MovimientoMapper mapper;

    private static final BigDecimal LIMITE_DIARIO = BigDecimal.valueOf(1000);
	
	public MovimientoServiceImpl(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository, MovimientoMapper mapper) {
		this.movimientoRepository = movimientoRepository;
		this.cuentaRepository = cuentaRepository;
		this.mapper = mapper;
	}

	@Override
	public MovimientoDTO crear(MovimientoDTO dto) {
		log.info("Creando movimiento para cuenta: {}", dto.getNumeroCuenta());

        Cuenta cuenta = cuentaRepository.findById(dto.getNumeroCuenta())
                .orElseThrow(() -> {
                    log.error("Cuenta {} no encontrada", dto.getNumeroCuenta());
                    return new RuntimeException("Cuenta no encontrada");
                });

        BigDecimal saldoActual = cuenta.getSaldoInicial();
        BigDecimal valorMovimiento = dto.getValor();

        log.error("verificando limites");
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = LocalDate.now().atTime(23, 59, 59, 999_999_999);
        List<Movimiento> movimientosHoy = movimientoRepository.findByCuentaAndFechaBetween(cuenta, inicioDia, finDia);

        BigDecimal totalDebitosHoy = movimientosHoy.stream()
                .filter(m -> "DEBITO".equalsIgnoreCase(m.getTipoMovimiento()))
                .map(Movimiento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if ("DEBITO".equalsIgnoreCase(dto.getTipoMovimiento())) {
            if (totalDebitosHoy.add(valorMovimiento).compareTo(LIMITE_DIARIO) > 0) {
                log.error("Cupo diario excedido para la cuenta {}", cuenta.getNumeroCuenta());
                throw new RuntimeException("Cupo diario Excedido");
            }
            if (saldoActual.compareTo(valorMovimiento) < 0) {
                log.error("Saldo no disponible en la cuenta {}", cuenta.getNumeroCuenta());
                throw new RuntimeException("Saldo no disponible");
            }
            saldoActual = saldoActual.subtract(valorMovimiento);
        } else if ("CREDITO".equalsIgnoreCase(dto.getTipoMovimiento())) {
            saldoActual = saldoActual.add(valorMovimiento);
        } else {
            log.error("Tipo de movimiento inválido: {}", dto.getTipoMovimiento());
            throw new RuntimeException("Tipo de movimiento inválido");
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());
        movimiento.setValor(valorMovimiento);
        movimiento.setSaldo(saldoActual);
        
        cuenta.setSaldoInicial(saldoActual);
        cuentaRepository.save(cuenta);

        Movimiento guardado = movimientoRepository.save(movimiento);
        log.info("Movimiento registrado con id: {}", guardado.getId());

        return mapper.toDTO(guardado);
	}

	@Override
	public List<MovimientoDTO> listarMovimientosPorCuenta(String numeroCuenta) {
		log.info("Listando movimientos de la cuenta: {}", numeroCuenta);
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        return movimientoRepository.findByCuenta(cuenta)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
	}

	@Override
	public List<MovimientoDTO> listarMovimientosPorCuentaYFecha(String numeroCuenta, LocalDateTime inicio,
			LocalDateTime fin) {
		log.info("Listando movimientos de la cuenta {} entre {} y {}", numeroCuenta, inicio, fin);
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        return movimientoRepository.findByCuentaAndFechaBetween(cuenta, inicio, fin)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
	}

}

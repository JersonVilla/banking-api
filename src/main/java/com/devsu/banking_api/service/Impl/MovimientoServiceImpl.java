package com.devsu.banking_api.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.MovimientoDTO;
import com.devsu.banking_api.dto.MovimientoResponseDTO;
import com.devsu.banking_api.exception.BadRequestException;
import com.devsu.banking_api.exception.NotFoundException;
import com.devsu.banking_api.mapper.MovimientoMapper;
import com.devsu.banking_api.model.entity.Cuenta;
import com.devsu.banking_api.model.entity.Movimiento;
import com.devsu.banking_api.model.repository.CuentaRepository;
import com.devsu.banking_api.model.repository.MovimientoRepository;
import com.devsu.banking_api.service.ImovimientoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements ImovimientoService {

	private final MovimientoRepository movimientoRepository;
	private final CuentaRepository cuentaRepository;
	private final MovimientoMapper mapper;

	private static final String DEPOSITO = "DEPOSITO";
    private static final String RETIRO = "RETIRO";
    
    private static final String CUENTA_NO_ENCONTRADA = "Cuenta no encontrada";
    private static final String TIPO_MOVIMIENTO_INVALIDO = "Tipo de movimiento inválido";
    private static final String CUPO_DIARIO_EXCEDIDO = "Cupo diario excedido";
    private static final String SALDO_NO_DISPONIBLE = "Saldo no disponible";
    
    private static final BigDecimal LIMITE_DIARIO = BigDecimal.valueOf(1000);

	@Override
	public MovimientoResponseDTO crear(MovimientoDTO dto) {
		log.info("Creando movimiento para cuenta {} tipo: {} valor: {}", dto.getNumeroCuenta(), dto.getTipoMovimiento(), dto.getValor());

		Cuenta cuenta = obtenerCuenta(dto.getNumeroCuenta());
		
        BigDecimal nuevoSaldo = calcularSaldo(cuenta, dto.getTipoMovimiento(), dto.getValor());

        Movimiento movimiento = guardarMovimiento(cuenta, dto.getTipoMovimiento(), dto.getValor(), nuevoSaldo);

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        log.info("Movimiento creado para cuenta {}. Saldo nuevo: {}", cuenta.getNumeroCuenta(), nuevoSaldo);
	    return construirResponse(cuenta, movimiento, dto.getValor(), nuevoSaldo);
    }
	
	@Override
	public List<MovimientoResponseDTO> listar() {
		List<Movimiento> movimientos = movimientoRepository.findAll();
        log.info("Listando {} movimientos", movimientos.size());
        return movimientos.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
	}
    
	@Override
	public List<MovimientoResponseDTO> listarMovimientosPorCuenta(String numeroCuenta) {
		Cuenta cuenta = obtenerCuenta(numeroCuenta);
        List<Movimiento> movimientos = movimientoRepository.findByCuenta(cuenta);
        log.info("Movimientos encontrados para cuenta {}: {}", numeroCuenta, movimientos.size());
        return movimientos.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
	}

	@Override
	public List<MovimientoResponseDTO> listarMovimientosPorCuentaYFecha(String numeroCuenta, LocalDateTime inicio,
			LocalDateTime fin) {
		Cuenta cuenta = obtenerCuenta(numeroCuenta);
        List<Movimiento> movimientos = movimientoRepository.findByCuentaAndFechaBetween(cuenta, inicio, fin);
        log.info("Movimientos encontrados para cuenta {} entre {} y {}: {}", numeroCuenta, inicio, fin, movimientos.size());
        return movimientos.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
	}
	
	private Cuenta obtenerCuenta(String numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new NotFoundException(CUENTA_NO_ENCONTRADA));
    }
	
	private BigDecimal calcularSaldo(Cuenta cuenta, String tipoMovimiento, BigDecimal valorMovimiento) {
        if (DEPOSITO.equalsIgnoreCase(tipoMovimiento)) {
            return cuenta.getSaldoInicial().add(valorMovimiento);
        } else if (RETIRO.equalsIgnoreCase(tipoMovimiento)) {
            validarRetiro(cuenta, valorMovimiento);
            return cuenta.getSaldoInicial().subtract(valorMovimiento);
        } else {
            throw new BadRequestException(TIPO_MOVIMIENTO_INVALIDO);
        }
    }

    private void validarRetiro(Cuenta cuenta, BigDecimal valorMovimiento) {
        if (valorMovimiento.compareTo(LIMITE_DIARIO) > 0) {
            throw new BadRequestException(CUPO_DIARIO_EXCEDIDO);
        }
        if (cuenta.getSaldoInicial().compareTo(valorMovimiento) < 0) {
            throw new BadRequestException(SALDO_NO_DISPONIBLE);
        }
    }
	
    private Movimiento guardarMovimiento(Cuenta cuenta, String tipoMovimiento, BigDecimal valorMovimiento, BigDecimal nuevoSaldo) {
        Movimiento movimiento = Movimiento.builder()
                .cuenta(cuenta)
                .tipoMovimiento(tipoMovimiento)
                .valor(RETIRO.equalsIgnoreCase(tipoMovimiento) ? valorMovimiento.negate() : valorMovimiento)
                .saldo(nuevoSaldo)
                .fecha(LocalDateTime.now())
                .build();
        return movimientoRepository.save(movimiento);
    }
    
    private MovimientoResponseDTO construirResponse(Cuenta cuenta, Movimiento movimiento, BigDecimal valorMovimiento, BigDecimal nuevoSaldo) {
        return MovimientoResponseDTO.builder()
                .fecha(movimiento.getFecha())
                .cliente(cuenta.getCliente().getNombre())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .saldoInicial(cuenta.getSaldoInicial())
                .estado(cuenta.getEstado())
                .movimiento(RETIRO.equalsIgnoreCase(movimiento.getTipoMovimiento()) ? valorMovimiento.negate() : valorMovimiento)
                .saldoDisponible(nuevoSaldo)
                .build();
    }

}

package com.devsu.banking_api.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsu.banking_api.model.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

	List<Movimiento> findByCuentaNumeroCuentaAndFechaBetween(String numeroCuenta, LocalDateTime inicio, LocalDateTime fin);
	
}

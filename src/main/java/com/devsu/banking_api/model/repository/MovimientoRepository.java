package com.devsu.banking_api.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.banking_api.model.entity.Cuenta;
import com.devsu.banking_api.model.entity.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

	List<Movimiento> findByCuenta(Cuenta cuenta);

    List<Movimiento> findByCuentaAndFechaBetween(Cuenta cuenta, LocalDateTime inicio, LocalDateTime fin);

    List<Movimiento> findByCuentaAndFechaBetweenAndTipoMovimiento(Cuenta cuenta, LocalDateTime inicio, LocalDateTime fin, String tipoMovimiento);
    
	
}

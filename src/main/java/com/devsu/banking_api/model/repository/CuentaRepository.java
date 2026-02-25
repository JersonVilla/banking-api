package com.devsu.banking_api.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.banking_api.model.entity.Cuenta;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, String>{
	
	List<Cuenta> findByEstadoTrue();
	
}

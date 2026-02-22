package com.devsu.banking_api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsu.banking_api.model.entity.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, String>{

}

package com.devsu.banking_api.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsu.banking_api.model.entity.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	boolean existsByIdentificacion(String identificacion);
	
	@Query(value = """
            SELECT p.id, p.nombre
            FROM clientes c
            INNER JOIN personas p ON p.id = c.id
            WHERE c.estado = true
            """, nativeQuery = true)
    List<Object[]> findClientesActivosNative();
	
}

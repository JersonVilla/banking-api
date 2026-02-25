package com.devsu.banking_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.banking_api.dto.CuentaDTO;
import com.devsu.banking_api.dto.CuentaResponseDTO;
import com.devsu.banking_api.service.ICuentaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class CuentaController {

	private final ICuentaService cuentaService;
	
	@PostMapping
	public ResponseEntity<CuentaResponseDTO> crear(@RequestBody CuentaDTO dto) {
		log.info("Solicitud POST /cuentas");
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.crear(dto));
    }

    @PutMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaResponseDTO> actualizar(
            @PathVariable String numeroCuenta,
            @RequestBody CuentaDTO dto) {
        return ResponseEntity.ok(cuentaService.actualizar(numeroCuenta, dto));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> listar() {
    	log.info("Solicitud GET /cuentas");
        return ResponseEntity.ok(cuentaService.listar());
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaResponseDTO> obtenerPorNumeroCuenta(@PathVariable String numeroCuenta) {
    	log.info("Solicitud GET /cuentas/{}", numeroCuenta);
        return ResponseEntity.ok(cuentaService.obtenerPorNumeroCuenta(numeroCuenta));
    }

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<Void> eliminar(@PathVariable String numeroCuenta) {
    	log.info("Solicitud DELETE /cuentas/{}", numeroCuenta);
        cuentaService.eliminar(numeroCuenta);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/activas")
    public ResponseEntity<List<CuentaDTO>> obtenerCuentasActivas() {
        List<CuentaDTO> cuentas = cuentaService.getCuentasActivas();
        return ResponseEntity.ok(cuentas);
    }
    
}

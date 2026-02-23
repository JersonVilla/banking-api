package com.devsu.banking_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.banking_api.dto.MovimientoDTO;
import com.devsu.banking_api.dto.MovimientoResponseDTO;
import com.devsu.banking_api.service.ImovimientoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200"})
public class MovimientoController {

	private final ImovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> crear(@RequestBody MovimientoDTO dto) {
        log.info("Solicitud POST /movimientos");
        return ResponseEntity.ok(movimientoService.crear(dto));
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<List<MovimientoResponseDTO>> listarMovimientosPorCuenta(@PathVariable String numeroCuenta) {
        log.info("Solicitud GET /movimientos/{}", numeroCuenta);
        return ResponseEntity.ok(movimientoService.listarMovimientosPorCuenta(numeroCuenta));
    }

    @GetMapping("/{numeroCuenta}/rango")
    public ResponseEntity<List<MovimientoResponseDTO>> listarMovimientosPorCuentaYFecha(
            @PathVariable String numeroCuenta,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        log.info("Solicitud GET /movimientos/{}/rango de {} a {}", numeroCuenta, inicio, fin);
        return ResponseEntity.ok(movimientoService.listarMovimientosPorCuentaYFecha(numeroCuenta, inicio, fin));
    }
    
}

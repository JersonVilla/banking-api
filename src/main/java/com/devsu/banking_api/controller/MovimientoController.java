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
import com.devsu.banking_api.service.IReporteService;
import com.devsu.banking_api.service.ImovimientoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/movimientos")
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200"})
public class MovimientoController {

	private final ImovimientoService movimientoService;
	private final IReporteService reporteService;

    public MovimientoController(ImovimientoService movimientoService, IReporteService reporteService) {
		this.movimientoService = movimientoService;
		this.reporteService = reporteService;
	}

	@PostMapping
    public ResponseEntity<MovimientoResponseDTO> crear(@RequestBody MovimientoDTO dto) {
        log.info("Solicitud POST /movimientos");
        return ResponseEntity.ok(movimientoService.crear(dto));
    }

	@GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listar() {
    	log.info("Solicitud GET /movimientos");
        return ResponseEntity.ok(movimientoService.listar());
    }
	
    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<List<MovimientoResponseDTO>> listarMovimientosPorCuenta(@PathVariable String numeroCuenta) {
        log.info("Solicitud GET /movimientos/{}", numeroCuenta);
        return ResponseEntity.ok(movimientoService.listarMovimientosPorCuenta(numeroCuenta));
    }
    
    @GetMapping("/reportes/{numeroCuenta}/rango")
    public ResponseEntity<byte[]> generarReporte(
            @PathVariable String numeroCuenta,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        try {
        	log.info("Solicitud GET /reportes/{}/rango de {} a {}", numeroCuenta, inicio, fin);
            List<MovimientoResponseDTO> movimientos = movimientoService
                    .listarMovimientosPorCuentaYFecha(numeroCuenta, inicio, fin);

            log.info("Generar PDF");
            byte[] pdf = reporteService.generarReporteMovimientos(movimientos);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"reporte_movimientos.pdf\"")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            log.error("Error generando reporte: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}

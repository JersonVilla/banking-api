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

import com.devsu.banking_api.dto.ClienteBasicDTO;
import com.devsu.banking_api.dto.ClienteDTO;
import com.devsu.banking_api.dto.ClienteResponseDTO;
import com.devsu.banking_api.service.IClienteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class ClienteController {

	private final IClienteService clienteService;
	
	@GetMapping
	public ResponseEntity<List<ClienteResponseDTO>> listar() {
		log.info("Solicitud GET /clientes");
		return ResponseEntity.ok(clienteService.listar());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
		log.info("Solicitud GET /clientes/{}", id);
		return ResponseEntity.ok(clienteService.obtenerPorId(id));
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ClienteDTO dto) {
		log.info("Solicitud PUT /clientes/{}", id);
        return ResponseEntity.ok(clienteService.actualizar(id, dto));
    }
	
	@PostMapping
	public ResponseEntity<ClienteResponseDTO> crear(@RequestBody ClienteDTO dto) {
		log.info("Solicitud POST /clientes");
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(clienteService.crear(dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id){
		log.info("Solicitud DELETE /clientes/{}", id);
		clienteService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<ClienteBasicDTO>> listarActivos() {
	    log.info("Solicitud GET /clientes/activos");
	    return ResponseEntity.ok(clienteService.listarActivos());
	}
	
}

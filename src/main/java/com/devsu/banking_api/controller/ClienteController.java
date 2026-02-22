package com.devsu.banking_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.banking_api.dto.ClienteDTO;
import com.devsu.banking_api.service.IClienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

	private final IClienteService clienteService;
	
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> listar() {
		return ResponseEntity.ok(clienteService.listar());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> obtenerPorId(@PathVariable Long id) {
		return ResponseEntity.ok(clienteService.obtenerPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<ClienteDTO> crear(@RequestBody ClienteDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(clienteService.crear(dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id){
		clienteService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
	
}

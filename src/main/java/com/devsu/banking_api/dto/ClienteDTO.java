package com.devsu.banking_api.dto;

import lombok.Data;

@Data
public class ClienteDTO {

	private Long id;
	private String nombre;
	private String genero;
	private Integer edad;
	private String identificacion;
	private String direccion;
	private String telefono;
	private String contrasena;
	private Boolean estado;
	
}

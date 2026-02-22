package com.devsu.banking_api.mapper;

import org.mapstruct.Mapper;

import com.devsu.banking_api.dto.ClienteDTO;
import com.devsu.banking_api.model.entity.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

	Cliente toEntity(ClienteDTO dto);
	
	ClienteDTO toDTO(Cliente cliente);
	
}

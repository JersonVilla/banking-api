package com.devsu.banking_api.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponseDTO<T> {

	private Integer code;
    private HttpStatus httpStatus;
    private String message;
    private T data;

    public GeneralResponseDTO(HttpStatus httpStatus, Integer code, String message, T data) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public GeneralResponseDTO(HttpStatus httpStatus, Integer code, String message) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public GeneralResponseDTO(HttpStatus httpStatus, String message, T data) {
        this(httpStatus, httpStatus.value(), message, data);
    }

    public GeneralResponseDTO(HttpStatus httpStatus, String message) {
        this(httpStatus, httpStatus.value(), message);
    }

    public GeneralResponseDTO(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.OK;
        this.code = this.httpStatus.value();
    }

    public GeneralResponseDTO(T data, String message) {
        this(message);
        this.data = data;
    }
    
}

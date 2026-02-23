package com.devsu.banking_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devsu.banking_api.dto.GeneralResponseDTO;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralResponseDTO<String> handlerRequestException(NotFoundException e) {
        return new GeneralResponseDTO<>(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralResponseDTO<String> handlerRequestException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).findFirst().orElse(e.getMessage());
        return new GeneralResponseDTO<>(HttpStatus.BAD_REQUEST, errorMsg);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralResponseDTO<String> handlerRequestException(BadRequestException e) {
        return new GeneralResponseDTO<>(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(GeneralException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GeneralResponseDTO<String> handlerRequestException(GeneralException e) {
        return new GeneralResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    
}

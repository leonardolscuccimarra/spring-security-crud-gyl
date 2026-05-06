package com.gyl.CrudGyl.exception.handler;

import com.gyl.CrudGyl.exception.RecursosNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursosNoEncontradoException.class)
    public ResponseEntity<String> handleNotFound(RecursosNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnything(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al procesar los datos, verifique el contenido del método\n"
                + ex.getMessage());
    }
}
package com.gyl.CrudGyl.controller;

import com.gyl.CrudGyl.dto.ProductResponseDto;
import com.gyl.CrudGyl.dto.ProductoRequestDto;
import com.gyl.CrudGyl.entity.Producto;
import com.gyl.CrudGyl.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto crear(@Valid @RequestBody ProductoRequestDto dto) {
        return productoService.crear(dto);
    }

    @GetMapping
    public List<ProductResponseDto> listar(){
        return productoService.listar();
    }
}
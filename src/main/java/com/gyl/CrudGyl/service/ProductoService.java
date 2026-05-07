package com.gyl.CrudGyl.service;

import com.gyl.CrudGyl.dto.producto.ProductResponseDto;
import com.gyl.CrudGyl.dto.producto.ProductoRequestDto;

import java.util.List;

public interface ProductoService {

   ProductResponseDto crear(ProductoRequestDto dto);

   List<ProductResponseDto> listar();

   ProductResponseDto buscarPorId(Long id);

   ProductResponseDto actualizar(Long id,  ProductoRequestDto dto);

   void eliminar(Long id);

   List<ProductResponseDto> busquedaNombre(String nombre);
}

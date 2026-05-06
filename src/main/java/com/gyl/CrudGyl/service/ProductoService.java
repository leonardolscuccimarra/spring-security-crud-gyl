package com.gyl.CrudGyl.service;

import com.gyl.CrudGyl.dto.ProductResponseDto;
import com.gyl.CrudGyl.dto.ProductoRequestDto;

import java.util.List;

public interface ProductoService {

   ProductResponseDto crear(ProductoRequestDto dto);

   List<ProductResponseDto> listar();

   ProductResponseDto buscarPorId(Long id);

   ProductResponseDto actualizar(Long id,  ProductoRequestDto dto);

   void eliminar(Long id);

   List<ProductResponseDto> busquedaNombre(String nombre);
}

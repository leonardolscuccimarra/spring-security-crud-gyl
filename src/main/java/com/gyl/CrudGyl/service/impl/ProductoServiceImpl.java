package com.gyl.CrudGyl.service.impl;

import com.gyl.CrudGyl.dto.ProductResponseDto;
import com.gyl.CrudGyl.dto.ProductoRequestDto;
import com.gyl.CrudGyl.entity.Producto;
import com.gyl.CrudGyl.exception.RecursosNoEncontradoException;
import com.gyl.CrudGyl.mapper.ProductoMapper;
import com.gyl.CrudGyl.repository.ProductoRepository;
import com.gyl.CrudGyl.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {


    private ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<ProductResponseDto> busquedaNombre(String nombre){
        return productoRepository.findByNombre(nombre)
                .stream()
                .map(ProductoMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto crear(ProductoRequestDto dto) {
        Producto producto = ProductoMapper.toEntity(dto);
        Producto guardado = productoRepository.save(producto);
        return ProductoMapper.toResponseDto(guardado);
    }

    @Override
    public List<ProductResponseDto> listar() {
        return productoRepository.findAll()
                .stream()
                .map(ProductoMapper::toResponseDto)
                .toList();
    }



    @Override
    public ProductResponseDto buscarPorId(Long id) {

        return productoRepository.findById(id)
                .map(ProductoMapper::toResponseDto)
                .orElseThrow(()-> new RecursosNoEncontradoException(
                        "No se encontro el Id " + id
                ));
    }

    @Override
    public ProductResponseDto actualizar(Long id, ProductoRequestDto dto) {
        Producto producto  = productoRepository.findById(id)
                        .orElseThrow(()-> new RecursosNoEncontradoException(
                                "No se encontro el id " + id
                        ));

        ProductoMapper.updateEntity(producto, dto);
        Producto guardado = productoRepository.save(producto);
        return ProductoMapper.toResponseDto(guardado);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto  = productoRepository.findById(id)
                .orElseThrow(()-> new RecursosNoEncontradoException(
                        "No se encontro el id " + id
                ));
        productoRepository.delete(producto);
    }
}

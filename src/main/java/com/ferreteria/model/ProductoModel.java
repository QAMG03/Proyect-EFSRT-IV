package com.ferreteria.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ferreteria.entity.Producto;
import com.ferreteria.persistence.ProductoRepository;
import com.ferreteria.usecase.ProductoUseCase;

@Service
public class ProductoModel implements ProductoUseCase{
	
	
	@Autowired
	private ProductoRepository productoRepository;

	@Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        try {
            productoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("el producto está siendo usado en una venta.");
        }
    }

	@Override
	public List<Producto> buscarPorNombre(String texto) {
		return productoRepository.findByNombreContainingIgnoreCase(texto.trim());
	}
}

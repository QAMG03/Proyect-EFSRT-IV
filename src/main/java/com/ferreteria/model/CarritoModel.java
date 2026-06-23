package com.ferreteria.model;

import com.ferreteria.entity.CarritoItem;
import com.ferreteria.entity.Producto;
import com.ferreteria.persistence.CarritoRepository;
import com.ferreteria.persistence.ProductoRepository;
import com.ferreteria.usecase.CarritoUseCase;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoModel implements CarritoUseCase {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void agregarProducto(String username, Long productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoItem item = new CarritoItem();
        item.setUsername(username);
        item.setProducto(producto);
        item.setCantidad(cantidad);

        carritoRepository.save(item);
    }

    @Override
    public List<CarritoItem> listarCarrito(String username) {
        return carritoRepository.findByUsername(username);
    }

    
    @Transactional
    @Override
    public void eliminarItem(String username, Long itemId) {
        carritoRepository.deleteByIdAndUsername(itemId, username);
    }
    @Transactional
    @Override
    public void vaciarCarrito(String username) {
        carritoRepository.deleteByUsername(username);
    }
}

package com.ferreteria.model;

import com.ferreteria.entity.MovimientoStock;
import com.ferreteria.entity.Producto;
import com.ferreteria.persistence.MovimientoStockRepository;
import com.ferreteria.persistence.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoStockModel {

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public MovimientoStock registrar(MovimientoStock movimiento) {
        Producto producto = productoRepository.findById(movimiento.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int cantidad = movimiento.getCantidad();
        switch (movimiento.getTipo()) {
            case ENTRADA -> producto.setStock(producto.getStock() + cantidad);
            case SALIDA -> {
                if (producto.getStock() < cantidad) {
                    throw new RuntimeException("Stock insuficiente para salida");
                }
                producto.setStock(producto.getStock() - cantidad);
            }
        }

        productoRepository.save(producto);
        return movimientoStockRepository.save(movimiento);
    }

    public List<MovimientoStock> listar() {
        return movimientoStockRepository.findAll();
    }
    
    public List<MovimientoStock> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Debe ingresar ambas fechas.");
        }

        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha 'Desde' no puede ser mayor que la fecha 'Hasta'.");
        }

        LocalDateTime desdeInicio = desde.atStartOfDay();
        LocalDateTime hastaFin = hasta.atTime(23, 59, 59);

        return movimientoStockRepository.buscarEntreFechas(desdeInicio, hastaFin);
    }

}

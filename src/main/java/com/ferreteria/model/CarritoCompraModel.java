package com.ferreteria.model;

import com.ferreteria.entity.*;
import com.ferreteria.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarritoCompraModel {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public void confirmarCompra(String username) {
        
        Cliente cliente = clienteRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado para el usuario: " + username));

        
        List<CarritoItem> items = carritoRepository.findByUsername(username);
        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío.");
        }

        
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(0.0); 
        venta = ventaRepository.save(venta);

        double totalVenta = 0.0;

        for (CarritoItem item : items) {
            Producto producto = item.getProducto();
            int cantidad = item.getCantidad();

            
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

           
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(cantidad * producto.getPrecio());
            detalleVentaRepository.save(detalle);

            totalVenta += detalle.getSubtotal();
        }

        venta.setTotal(totalVenta);
        ventaRepository.save(venta);

        
        carritoRepository.deleteAll(items);
    }
}

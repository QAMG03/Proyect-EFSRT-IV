package com.ferreteria.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferreteria.entity.CarritoItem;
import com.ferreteria.entity.Cliente;
import com.ferreteria.entity.DetalleVenta;
import com.ferreteria.entity.Producto;
import com.ferreteria.entity.Venta;
import com.ferreteria.persistence.CarritoRepository;
import com.ferreteria.persistence.ClienteRepository;
import com.ferreteria.persistence.DetalleVentaRepository;
import com.ferreteria.persistence.ProductoRepository;
import com.ferreteria.persistence.VentaRepository;
import com.ferreteria.usecase.VentaUseCase;

import jakarta.transaction.Transactional;

@Service
public class VentaModel implements VentaUseCase {

    @Autowired private VentaRepository ventaRepo;
    @Autowired private ProductoRepository productoRepo;
    @Autowired private DetalleVentaRepository detalleRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private CarritoRepository carritoRepo;
    
    @Override
    public List<Venta> obtenerVentasPorUsername(String username) {
        Cliente cliente = clienteRepo.findByUsuarioUsername(username)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        return ventaRepo.findByClienteId(cliente.getId());
    }
    
    
    @Transactional
    @Override
    public void registrarVenta(Venta venta) {
        Cliente cliente = clienteRepo.findById(venta.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!"ACTIVO".equalsIgnoreCase(cliente.getEstado())) {
            throw new RuntimeException("El cliente está inactivo y no puede realizar compras.");
        }

        double total = 0.0;

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = productoRepo.findById(detalle.getProducto().getId())
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad del producto no puede ser menor o igual a cero.");
            }

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre() +
                                           ". Solo quedan " + producto.getStock() + " unidades.");
            }

           
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepo.save(producto);

            
            detalle.setProducto(producto);
            detalle.setVenta(venta);

            total += detalle.getCantidad() * producto.getPrecio();
        }

        venta.setTotal(total);
        ventaRepo.save(venta);
    }

    @Transactional
    public Venta registrarVenta(String username) {
       
        Cliente cliente = clienteRepo.findByUsuarioUsername(username)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado para el usuario: " + username));

        if (!"ACTIVO".equalsIgnoreCase(cliente.getEstado())) {
            throw new RuntimeException("El cliente está inactivo y no puede comprar.");
        }

        
        List<CarritoItem> carrito = carritoRepo.findByUsername(username);
        if (carrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío.");
        }

        Venta venta = new Venta();
        venta.setCliente(cliente);
        List<DetalleVenta> detalles = new ArrayList<>();
        double total = 0;

        for (CarritoItem item : carrito) {
            Producto producto = item.getProducto();

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepo.save(producto);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setVenta(venta);
            detalles.add(detalle);

            total += producto.getPrecio() * item.getCantidad();
        }

        venta.setDetalles(detalles);
        venta.setTotal(total);

        ventaRepo.save(venta);
        detalleRepo.saveAll(detalles);
        carritoRepo.deleteAll(carrito);

        return venta;
    }
    
    @Override
    public List<Venta> listar() {
        return ventaRepo.findAll();
    }

    @Override
    public Venta obtenerPorId(Long id) {
        return ventaRepo.findById(id).orElse(null);
    }

    @Override
    public void eliminarPorId(Long id) {
        ventaRepo.deleteById(id);
    }

}



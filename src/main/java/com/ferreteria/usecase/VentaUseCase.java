package com.ferreteria.usecase;



import java.util.List;

import org.springframework.stereotype.Component;

import com.ferreteria.entity.Venta;

@Component
public interface VentaUseCase {
    void registrarVenta(Venta venta);
    List<Venta> listar();
    Venta obtenerPorId(Long id);
    void eliminarPorId(Long id);
    List<Venta> obtenerVentasPorUsername(String username);
}


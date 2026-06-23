package com.ferreteria.usecase;

import com.ferreteria.entity.CarritoItem;

import java.util.List;

public interface CarritoUseCase {

    void agregarProducto(String username, Long productoId, int cantidad);

    List<CarritoItem> listarCarrito(String username);

    void eliminarItem(String username, Long itemId);

    void vaciarCarrito(String username);
}

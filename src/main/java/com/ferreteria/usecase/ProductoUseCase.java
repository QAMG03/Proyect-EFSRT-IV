package com.ferreteria.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import com.ferreteria.entity.Producto;


@Component
public interface ProductoUseCase {
	List<Producto> listar();
    Producto guardar(Producto producto);
    Producto obtenerPorId(Long id);
    void eliminar(Long id);
    
    
    //-----
    List<Producto> buscarPorNombre(String texto);
}

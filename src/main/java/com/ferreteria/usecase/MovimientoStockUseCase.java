package com.ferreteria.usecase;


import java.util.List;



import com.ferreteria.entity.MovimientoStock;


public interface MovimientoStockUseCase {
	List<MovimientoStock> listar();
    void registrar(MovimientoStock movimiento);
	List<MovimientoStock> obtenerTodos();
	
}

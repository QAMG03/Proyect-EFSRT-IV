package com.ferreteria.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferreteria.entity.MovimientoStock;
import com.ferreteria.persistence.MovimientoStockRepository;

@Service
public class MovimientoStockService implements MovimientoStockUseCase {

    @Autowired
    private MovimientoStockRepository movimientoRepo;

    @Override
    public List<MovimientoStock> listar() {
        return movimientoRepo.findAll();
    }

    @Override
    public void registrar(MovimientoStock movimiento) {
        movimientoRepo.save(movimiento);
    }

    @Override
    public List<MovimientoStock> obtenerTodos() {
        return movimientoRepo.findAll(); 
    }
}

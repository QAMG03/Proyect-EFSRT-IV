package com.ferreteria.usecase;

import com.ferreteria.entity.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ClienteUseCase {
    List<Cliente> listar();
    Cliente guardar(Cliente cliente);
    Cliente obtenerPorId(Long id);
    void suspender(Long id);
    List<Cliente> buscarPorNombreODni(String texto);
    public boolean eliminarDefinitivo(Long idCliente);
}

package com.ferreteria.model;

import com.ferreteria.entity.Cliente;
import com.ferreteria.entity.Venta;
import com.ferreteria.persistence.ClienteRepository;
import com.ferreteria.persistence.VentaRepository;
import com.ferreteria.usecase.ClienteUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteModel implements ClienteUseCase {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Cliente> listar() {
    	return clienteRepository.findAll();
    }

    
    @Override
    public List<Cliente> buscarPorNombreODni(String texto) {
        return clienteRepository.findByNombresContainingIgnoreCaseOrDniContainingAndEstado(
            texto.trim(), texto.trim(), "ACTIVO"
        );
    }
    
    @Override
    public Cliente guardar(Cliente cliente) {
    
        Cliente existente = clienteRepository.findByDni(cliente.getDni());

        
        if (existente != null && !existente.getId().equals(cliente.getId())) {
            throw new RuntimeException("El DNI ya está registrado.");
        }

        return clienteRepository.save(cliente);
    }


    @Override
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public void suspender(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente != null) {
            cliente.setEstado("INACTIVO");
            clienteRepository.save(cliente);
        }
    }

   

    public boolean eliminarDefinitivo(Long idCliente) {
        Optional<Cliente> clienteDel = clienteRepository.findById(idCliente);
        
        if (clienteDel.isPresent()) {
            Cliente cliente = clienteDel.get();

            
            List<Venta> ventas = ventaRepository.findByClienteId(idCliente);

            if (ventas == null || ventas.isEmpty()) {
                clienteRepository.delete(cliente); 
                return true;
            }
        }

        return false; 
    }

}

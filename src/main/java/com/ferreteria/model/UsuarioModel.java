package com.ferreteria.model;

import com.ferreteria.entity.Cliente;
import com.ferreteria.entity.Usuario;
import com.ferreteria.persistence.ClienteRepository;
import com.ferreteria.persistence.UsuarioRepository;
import com.ferreteria.usecase.UsuarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioModel implements UsuarioUseCase {

	@Autowired
    private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	
	@Override
	public Optional<Usuario> buscarUsuarioPorId(Long id) {
	    return obtenerUsuarioPorId(id);
	}
	
	@Override
	public List<Usuario> listarUsuarios() {
	    List<Usuario> usuarios = usuarioRepository.findAll();

	    for (Usuario u : usuarios) {
	        Cliente c = clienteRepository.findByUsuarioId(u.getId()); 
	        if (c != null) {
	            u.setNombres(c.getNombres());
	            u.setApellidos(c.getApellidos());
	            u.setDni(c.getDni()); 
	            u.setDireccion(c.getDireccion());
	            u.setTelefono(c.getTelefono());
	        }
	    }

	    return usuarios;
	}

	@Override
	public Optional<Usuario> obtenerUsuarioPorId(Long id) {
	    Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

	    optionalUsuario.ifPresent(usuario -> {
	        Cliente cliente = clienteRepository.findByUsuarioId(usuario.getId());
	        if (cliente != null) {
	            usuario.setNombres(cliente.getNombres());
	            usuario.setApellidos(cliente.getApellidos());
	            usuario.setDni(cliente.getDni());
	            usuario.setDireccion(cliente.getDireccion());
	            usuario.setTelefono(cliente.getTelefono());
	        }
	    });

	    return optionalUsuario;
	}




	@Override
	public void actualizarUsuario(Usuario usuario) {
	    
	    Usuario actual = usuarioRepository.findById(usuario.getId())
	        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    actual.setRol(usuario.getRol());
	    actual.setEstado(usuario.getEstado());

	    if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
	    	actual.setPassword(passwordEncoder.encode(usuario.getPassword()));
	    }

	    usuarioRepository.save(actual);

	    
	    Cliente cliente = clienteRepository.findByUsuarioId(usuario.getId());
	    if (cliente != null) {
	        cliente.setNombres(usuario.getNombres());
	        cliente.setApellidos(usuario.getApellidos());
	        cliente.setDni(usuario.getDni());
	        cliente.setDireccion(usuario.getDireccion());
	        cliente.setTelefono(usuario.getTelefono());
	        clienteRepository.save(cliente);
	    }
	}


	@Override
	public void eliminarUsuario(Long id) {
	    try {
	        Cliente cliente = clienteRepository.findByUsuarioId(id);
	        if (cliente != null) {
	            clienteRepository.delete(cliente);
	        }

	        usuarioRepository.deleteById(id);
	    } catch (DataIntegrityViolationException e) {
	        throw new RuntimeException("No se puede eliminar el usuario porque está vinculado a una venta.");
	    }
	}

    @Override
    @Transactional
    public void cambiarEstado(Long id) {
        Usuario u = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nuevoEstado = "ACTIVO".equals(u.getEstado()) ? "INACTIVO" : "ACTIVO";

        usuarioRepository.actualizarEstado(id, nuevoEstado);
    }

    
}

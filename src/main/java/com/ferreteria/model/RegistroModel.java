package com.ferreteria.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ferreteria.entity.Cliente;
import com.ferreteria.entity.Usuario;
import com.ferreteria.persistence.ClienteRepository;
import com.ferreteria.persistence.UsuarioRepository;
import com.ferreteria.usecase.RegistroUseCase;

@Service
public class RegistroModel implements RegistroUseCase {
	 @Autowired
	    private UsuarioRepository usuarioRepository;

	    @Autowired
	    private ClienteRepository clienteRepository;

	    @Autowired
	    private BCryptPasswordEncoder passwordEncoder;

	    
	    public void registrarUsuario(Usuario usuario) {
	        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
	            throw new RuntimeException("El nombre de usuario ya existe.");
	        }

	        if (clienteRepository.existsByDni(usuario.getDni())) {
	            throw new RuntimeException("El DNI ya está registrado.");
	        }

	        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	        usuario.setRol("USER");
	        usuario.setEstado("ACTIVO");
	        usuarioRepository.save(usuario);

	        Cliente cliente = new Cliente();
	        cliente.setNombres(usuario.getNombres());
	        cliente.setApellidos(usuario.getApellidos());
	        cliente.setDni(usuario.getDni());
	        cliente.setDireccion(usuario.getDireccion());
	        cliente.setTelefono(usuario.getTelefono());
	        cliente.setEstado("ACTIVO");
	        cliente.setUsuario(usuario);

	        clienteRepository.save(cliente);
	    }
}
